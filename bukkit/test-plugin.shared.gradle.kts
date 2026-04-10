import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URL
import java.nio.file.Files

/*
 * Shared Gradle script for bukkit/test_plugin_* projects.
 *
 * Each test plugin's build.gradle.kts should define only version-specific differences via extra
 * properties, then call:
 *   apply(from = "../test-plugin.shared.gradle.kts")
 *
 * Required properties:
 * - paperApiDependency: compileOnly dependency notation for the target Paper API.
 * - javaVersion: Java toolchain version as a stringified integer.
 *
 * Optional properties:
 * - serverJarDownloads:
 *   Format: "<url>=><relative path>" entries joined by "|".
 *   Example:
 *   "https://.../paper.jar=>server/server.jar|https://.../paper-mojmap.jar=>server_mojmap/server.jar"
 *   Parsing rules:
 *   - First split by "|" into download entries.
 *   - Then split each entry by "=>" into URL and destination path.
 *   - Destination path is resolved relative to the test plugin directory.
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
 * - includePatchedJarTask:
 *   "true" to register generatePatchedJar for the legacy 1.16.5 setup.
 *
 * Minimal example:
 *   extra["paperApiDependency"] = "io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT"
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

fun Project.optionalStringProperty(name: String): String? = findProperty(name)?.toString()

fun Project.optionalBooleanProperty(name: String): Boolean =
    optionalStringProperty(name)?.equals("true", ignoreCase = true) == true

fun Project.optionalListProperty(name: String): List<String> =
    optionalStringProperty(name)
        ?.split("|")
        ?.map(String::trim)
        ?.filter(String::isNotEmpty)
        .orEmpty()

val paperApiDependency = project.requiredStringProperty("paperApiDependency")
val javaVersion = project.requiredStringProperty("javaVersion").toInt()
val serverJarDownloads = project.optionalListProperty("serverJarDownloads")
val copyTargets = project.optionalListProperty("copyTargets").ifEmpty { listOf("server/plugins") }
val includeProtocolLib = project.optionalBooleanProperty("includeProtocolLib")
val includePatchedJarTask = project.optionalBooleanProperty("includePatchedJarTask")

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
    add("compileOnly", paperApiDependency)
    add("compileOnly", "com.mojang:brigadier:1.0.18")
    add("implementation", "com.opencsv:opencsv:5.9")
}

configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    sourceSets.named("main") {
        java.srcDirs("../src/main/java", "../../common/src/main/java", "../test_plugin_common/src/main/java")
        resources.srcDirs("../src/main/resources")
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

if (serverJarDownloads.isNotEmpty()) {
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
}

if (includePatchedJarTask) {
    tasks.register("generatePatchedJar") {
        group = "setup"
        dependsOn("downloadServerJar")
        doLast {
            val serverDir = projectDir.toPath().resolve("server").toFile()
            val patchedJar = serverDir.toPath().resolve("cache/patched_1.16.5.jar").toFile()
            if (!patchedJar.exists()) {
                val process = Runtime.getRuntime()
                    .exec("java -jar ${serverDir.resolve("server.jar")} nogui", arrayOf(), serverDir)
                process.waitFor()
                process.destroy()
            }
        }
    }
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
