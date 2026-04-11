import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URL
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/*
 * Shared Gradle script for bukkit-integration-test/fixtures/test-plugin-* projects.
 *
 * Each test plugin's build.gradle.kts should define only version-specific differences via extra
 * properties, then call:
 *   apply(from = "../test-plugin.shared.gradle.kts")
 *
 * Required properties:
 * - bukkitApiDependency: compileOnly dependency notation for the target Bukkit-compatible API.
 * - javaVersion: Java toolchain version as a stringified integer.
 * - platform: "paper" or "mohist".
 *
 * - minecraftServerVersion:
 *   Version string used by generated NMS jar paths. This is separate from the API dependency because
 *   some fixtures intentionally use names such as test-plugin-1.21.0 while Paper stores jars under 1.21.
 *
 * - serverJarDownloads:
 *   Format: "<url>=><relative path>" entries joined by "|".
 *   Example:
 *   "https://.../paper.jar=>server/server.jar|https://.../paper-mojmap.jar=>server_mojmap/server.jar"
 *   Parsing rules:
 *   - First split by "|" into download entries.
 *   - Then split each entry by "=>" into URL and destination path.
 *   - Destination path is resolved relative to the test plugin directory.
 *
 * Optional properties:
 *
 * - copyTargets:
 *   Format: "<relative dir>|<relative dir>|..."
 *   Default: "server/plugins"
 *   Example:
 *   "server/plugins|server_mojmap/plugins"
 *
 * - includeProtocolLib:
 *   "true" to register copyProtocolLibToServer.
 *
 * - nmsJarPaths:
 *   Explicit relative paths checked by generatePatchedJar, joined by "|".
 *   Use this when a server distribution has a layout that cannot be inferred from platform.
 *
 * - nmsGenerationServerJar:
 *   Server jar name used by generatePatchedJar. Default: "server.jar".
 *
 * Minimal example:
 *   extra["bukkitApiDependency"] = "io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT"
 *   extra["javaVersion"] = "17"
 *   extra["serverJarDownloads"] =
 *       "https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/496/downloads/paper-1.20.4-496.jar=>server/server.jar"
 *   apply(from = "../test-plugin.shared.gradle.kts")
 */

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("com.gradleup.shadow:shadow-gradle-plugin:9.4.1")
    }
}

apply(plugin = "java")
apply<ShadowPlugin>()

fun Project.requiredStringProperty(name: String): String =
    findProperty(name)?.toString() ?: error("Missing project property: $name")

fun Project.requiredListProperty(name: String): List<String> =
    requiredStringProperty(name)
        ?.split("|")
        ?.map(String::trim)
        ?.filter(String::isNotEmpty)
        ?: error("Missing project property: $name")

fun Project.optionalStringProperty(name: String): String? = findProperty(name)?.toString()

fun Project.optionalBooleanProperty(name: String): Boolean =
    optionalStringProperty(name)?.equals("true", ignoreCase = true) == true

fun Project.optionalListProperty(name: String): List<String> =
    optionalStringProperty(name)
        ?.split("|")
        ?.map(String::trim)
        ?.filter(String::isNotEmpty)
        .orEmpty()

val bukkitApiDependency = project.requiredStringProperty("bukkitApiDependency")
val javaVersion = project.requiredStringProperty("javaVersion").toInt()
val platform = project.requiredStringProperty("platform")
val minecraftServerVersion = project.requiredStringProperty("minecraftServerVersion")
val serverJarDownloads = project.requiredListProperty("serverJarDownloads")
val copyTargets = project.optionalListProperty("copyTargets").ifEmpty { listOf("server/plugins") }
val includeProtocolLib = project.optionalBooleanProperty("includeProtocolLib")
val explicitNmsJarPaths = project.optionalListProperty("nmsJarPaths")
val nmsGenerationServerJar = project.optionalStringProperty("nmsGenerationServerJar") ?: "server.jar"
val javaToolchains = project.extensions.getByType<JavaToolchainService>()

group = "net.kunmc.lab"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    flatDir { dirs("server/cache", "libs") }
}

dependencies {
    add("compileOnly", bukkitApiDependency)
    add("compileOnly", "com.mojang:brigadier:1.0.18")
}

configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    sourceSets.named("main") {
        java.srcDirs(
            "../../../bukkit/src/main/java",
            "../../../common/src/main/java",
            "../test-plugin-common/src/main/java"
        )
        resources.srcDirs("../../../bukkit/src/main/resources")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.named<Jar>("jar") {
    doFirst {
        copy {
            from(".")
            into(layout.buildDirectory.dir("resources/main"))
            include("LICENSE*")
        }
    }
}

val projectGroup = project.group.toString()
val projectNameLower = project.name.lowercase()
tasks.named<ShadowJar>("shadowJar") {
    mergeServiceFiles()
    archiveFileName.set("${rootProject.name}-${project.version}.jar")
    relocate("net.kunmc.lab.commandlib", "$projectGroup.$projectNameLower.commandlib")
    relocate("net.kunmc.lab.configlib", "$projectGroup.$projectNameLower.configlib")
    exclude("net/minecraft")
    exclude("org/bukkit")
    exclude("com/mojang")
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named("jar") {
    finalizedBy(tasks.named("shadowJar"))
}

tasks.named<Copy>("processResources") {
    val props = mapOf(
        "name" to rootProject.name,
        "version" to version,
        "MainClass" to getMainClassFQDN(projectDir.toPath())
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.register("copyToServer") {
    mustRunAfter("build")
    doLast {
        copyTargets.forEach { target ->
            copy {
                from(File(layout.buildDirectory.get().asFile.absolutePath, "libs/${rootProject.name}-${version}.jar"))
                into(target)
            }
        }
    }
}

if (includeProtocolLib) {
    tasks.register<Copy>("copyProtocolLibToServer") {
        group = "copy"
        configurations["compileClasspath"].files
            .stream()
            .filter { it.name.matches(Regex(".*ProtocolLib.*\\.jar")) }
            .findFirst()
            .ifPresent { file ->
                from(file)
                into("server/plugins")
            }
    }
}

tasks.register("buildAndCopy") {
    group = "build"
    dependsOn("build", "copyToServer")
}

tasks.register("downloadServerJar") {
    doLast {
        serverJarDownloads.forEach { download ->
            val (urlString, relativePath) = download.split("=>", limit = 2).let { parts ->
                require(parts.size == 2) { "Invalid serverJarDownloads entry: $download" }
                parts[0].trim() to parts[1].trim()
            }
            val outputFile = projectDir.toPath().resolve(relativePath).toFile()
            if (!outputFile.exists()) {
                outputFile.parentFile.mkdirs()
                URL(urlString).openStream().use { input ->
                    outputFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}

tasks.register("generatePatchedJar") {
    group = "setup"
    dependsOn("downloadServerJar")
    doLast {
        val serverDir = projectDir.toPath().resolve("server").toFile()
        val nmsJars = resolveNmsJarPaths(serverDir.toPath(), minecraftServerVersion, platform, explicitNmsJarPaths)
        if (nmsJars.any { !it.toFile().exists() }) {
            serverDir.resolve("eula.txt").writeText("eula=true\n")
            serverDir.resolve("server.properties").writeText(
                """
                online-mode=false
                server-port=25565
                enforce-secure-profile=false
                motd=CommandLib NMS Jar Generation
                """.trimIndent() + "\n"
            )
            val javaExecutable = javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(javaVersion))
            }.get().executablePath.asFile.absolutePath
            runServerUntilNmsJarsExist(serverDir, javaExecutable, nmsGenerationServerJar, nmsJars)
        }
        val missingJars = nmsJars.filter { !it.toFile().exists() }
        if (missingJars.isNotEmpty()) {
            error("Server startup finished but NMS jar(s) were not generated: ${missingJars.joinToString()}")
        }
    }
}

fun runServerUntilNmsJarsExist(
    serverDir: File,
    javaExecutable: String,
    serverJarName: String,
    nmsJars: List<java.nio.file.Path>
) {
    val process = ProcessBuilder(javaExecutable, "-jar", serverJarName, "nogui")
        .directory(serverDir)
        .redirectErrorStream(true)
        .start()

    val output = StringBuilder()
    val ready = AtomicBoolean(false)
    val outputThread = Thread {
        process.inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                output.appendLine(line)
                logger.lifecycle("[${project.name}] $line")
                if (line.contains("Done (") || line.contains("Done(")) {
                    ready.set(true)
                }
            }
        }
    }
    outputThread.isDaemon = true
    outputThread.start()

    val started = System.nanoTime()
    val timeoutNanos = TimeUnit.MINUTES.toNanos(10)
    while (process.isAlive && System.nanoTime() - started < timeoutNanos) {
        if (nmsJars.all { it.toFile().exists() } || ready.get()) {
            break
        }
        Thread.sleep(500)
    }

    if (process.isAlive) {
        process.outputStream.bufferedWriter().use {
            it.write("stop")
            it.newLine()
            it.flush()
        }
        if (!process.waitFor(2, TimeUnit.MINUTES)) {
            process.destroyForcibly()
        }
    } else {
        process.waitFor()
    }
    outputThread.join(TimeUnit.SECONDS.toMillis(10))

    if (System.nanoTime() - started >= timeoutNanos && nmsJars.any { !it.toFile().exists() }) {
        error("Timed out while waiting for NMS jar generation. Last output:\n$output")
    }
}

fun resolveNmsJarPaths(
    serverDir: java.nio.file.Path,
    minecraftServerVersion: String,
    platform: String,
    explicitNmsJarPaths: List<String>
): List<java.nio.file.Path> {
    if (explicitNmsJarPaths.isNotEmpty()) {
        return explicitNmsJarPaths.map { serverDir.resolve(it) }
    }

    return when (platform) {
        "paper" -> {
            if (isPaperLegacyPatchedJarVersion(minecraftServerVersion)) {
                listOf(serverDir.resolve("cache/patched_$minecraftServerVersion.jar"))
            } else {
                listOf(serverDir.resolve("versions/$minecraftServerVersion/paper-$minecraftServerVersion.jar"))
            }
        }

        "mohist" -> {
            listOf(
                serverDir.resolve("libraries/net/minecraft/server/$minecraftServerVersion/server-$minecraftServerVersion.jar"),
                serverDir.resolveFirstMatchingJar("libraries/net/minecraftforge/forge", ".*-server\\.jar"),
                serverDir.resolveFirstMatchingJar("libraries/net/minecraftforge/forge", ".*-universal\\.jar"),
            )
        }

        else -> error("Unsupported platform: $platform")
    }
}

fun isPaperLegacyPatchedJarVersion(version: String): Boolean {
    val parts = version.split(".").mapNotNull(String::toIntOrNull)
    val minor = parts.getOrNull(1) ?: return false
    val patch = parts.getOrNull(2) ?: 0
    return minor < 18 && !(minor == 17 && patch > 2)
}

fun java.nio.file.Path.resolveFirstMatchingJar(relativeRoot: String, fileNameRegex: String): java.nio.file.Path {
    val root = resolve(relativeRoot)
    if (!Files.exists(root)) {
        return root.resolve(fileNameRegex)
    }
    return Files.walk(root)
        .filter { Files.isRegularFile(it) }
        .filter { it.fileName.toString().matches(fileNameRegex.toRegex()) }
        .findFirst()
        .orElse(root.resolve(fileNameRegex))
}

fun getMainClassFQDN(projectPath: java.nio.file.Path): String {
    val mainClassFile = Files.walk(projectPath)
        .filter { it.fileName.toString().endsWith(".java") }
        .filter { file -> Files.lines(file).use { lines -> lines.anyMatch { it.contains("extends JavaPlugin") } } }
        .findFirst()
        .get()
    return mainClassFile.toString()
        .replace("\\", ".")
        .replace("/", ".")
        .replace(Regex(".*src.main.java.|.java$"), "")
}
