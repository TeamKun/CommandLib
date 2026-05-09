import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

abstract class CommandLibIntegrationSlot : BuildService<BuildServiceParameters.None>

repositories {
    mavenCentral()
    maven("https://repo.opencollab.dev/main/") {
        name = "opencollab-main"
    }
    maven("https://repo.opencollab.dev/maven-releases/") {
        name = "opencollab-releases"
    }
    maven("https://repo.opencollab.dev/maven-snapshots/") {
        name = "opencollab-snapshots"
    }
    maven("https://repo.codemc.io/repository/maven-public/") {
        name = "codemc-public"
    }
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-snapshots"
    }
    maven("https://oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-legacy-snapshots"
    }
    maven("https://jitpack.io") {
        name = "jitpack"
    }
}

val targetName = project.name
val targetPlatform = extra["commandlib.integration.platform"].toString()
val minecraftVersion = extra["commandlib.integration.minecraftVersion"].toString()
val minecraftJavaVersion = extra["commandlib.integration.javaVersion"].toString().toInt()
val serverDirectory = if (extra.has("commandlib.integration.serverDirectory")) {
    extra["commandlib.integration.serverDirectory"].toString()
} else {
    "server"
}
val serverJarName = if (extra.has("commandlib.integration.serverJarName")) {
    extra["commandlib.integration.serverJarName"].toString()
} else {
    "server.jar"
}
val bootstrapServerPort = if (extra.has("commandlib.integration.bootstrapServerPort")) {
    extra["commandlib.integration.bootstrapServerPort"].toString().toInt()
} else {
    25565
}
val requiresMohistBootstrap = if (extra.has("commandlib.integration.requiresMohistBootstrap")) {
    extra["commandlib.integration.requiresMohistBootstrap"].toString()
} else {
    "false"
}
    .toBooleanStrictOrNull()
    ?: false
val reportFileName = "TEST-commandlib-$minecraftVersion-$targetPlatform.xml"

val mcProtocol = configurations.named("mcProtocol")
val sourceSets = extensions.getByType<SourceSetContainer>()
val minecraftIntegrationMaxParallel = providers.gradleProperty("commandlib.minecraftIntegrationMaxParallel")
    .map(String::toInt)
    .orElse(4)
val mohistBootstrapMaxParallel = providers.gradleProperty("commandlib.mohistBootstrapMaxParallel")
    .map(String::toInt)
    .orElse(4)
val minecraftIntegrationSlot = gradle.sharedServices.registerIfAbsent(
    "commandlibMinecraftIntegrationSlot",
    CommandLibIntegrationSlot::class,
) {
    maxParallelUsages.set(minecraftIntegrationMaxParallel)
}
val mohistBootstrapSlot = gradle.sharedServices.registerIfAbsent(
    "commandlibMohistBootstrapSlot",
    CommandLibIntegrationSlot::class,
) {
    maxParallelUsages.set(mohistBootstrapMaxParallel)
}

dependencies {
    "testImplementation"(platform("org.junit:junit-bom:5.10.2"))
    "testImplementation"("org.junit.jupiter:junit-jupiter")
    "testImplementation"("org.assertj:assertj-core:3.25.3")
    "testImplementation"("org.awaitility:awaitility:4.2.1")
    "testImplementation"("org.testcontainers:junit-jupiter:1.21.4")
    "testImplementation"("org.testcontainers:testcontainers:1.21.4")
    "testRuntimeOnly"("org.slf4j:slf4j-simple:1.7.36")
    "testImplementation"("net.kyori:adventure-text-serializer-gson:4.15.0")
    "testImplementation"("net.kyori:adventure-text-serializer-json-legacy-impl:4.15.0")
}

configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    sourceSets.named("test") {
        java.srcDir(rootProject.file("integration-test/src/test/java"))
    }
}

tasks.named<Test>("test") {
    enabled = false
}

val nestedGradleUserHome = rootProject.file(".gradle-user-home-bukkit-it")
val integrationTestDir = rootProject.file("integration-test")
val sharedDir = integrationTestDir.resolve("shared")
val testPluginDir = project.file("test-plugin")
val isWindows = System.getProperty("os.name").startsWith("Windows", ignoreCase = true)
val javaToolchains = extensions.getByType<JavaToolchainService>()

val prepareTask = tasks.register<Exec>("prepareTestPlugin") {
    val hasWrapperJar = testPluginDir.resolve("gradle/wrapper/gradle-wrapper.jar").isFile

    workingDir(if (hasWrapperJar) testPluginDir else rootProject.projectDir)
    environment("GRADLE_USER_HOME", nestedGradleUserHome.absolutePath)

    if (isWindows) {
        if (hasWrapperJar) {
            commandLine("cmd", "/c", ".\\gradlew.bat", "buildAndCopy", "downloadServerJar")
        } else {
            commandLine(
                "cmd",
                "/c",
                ".\\gradlew.bat",
                "-p",
                testPluginDir.absolutePath,
                "buildAndCopy",
                "downloadServerJar",
            )
        }
    } else {
        if (hasWrapperJar) {
            commandLine("sh", "./gradlew", "buildAndCopy", "downloadServerJar")
        } else {
            commandLine(
                "sh",
                "./gradlew",
                "-p",
                testPluginDir.absolutePath,
                "buildAndCopy",
                "downloadServerJar",
            )
        }
    }
}

fun writeServerProperties(serverDir: File, port: Int) {
    serverDir.mkdirs()
    serverDir.resolve("server.properties").writeText(
        """
        online-mode=false
        server-port=$port
        enforce-secure-profile=false
        motd=CommandLib IT
        gamemode=creative
        force-gamemode=true
        difficulty=peaceful
        spawn-protection=0
        view-distance=2
        simulation-distance=2
        max-world-size=16
        generate-structures=false
        allow-nether=false
        level-type=flat
        """.trimIndent() + "\n"
    )
}

val writeDockerServerProperties = tasks.register("writeDockerServerProperties") {
    group = "setup"
    description = "Writes server.properties for the Docker-based integration test."
    dependsOn(prepareTask)

    val serverDir = testPluginDir.resolve(serverDirectory)

    doLast {
        writeServerProperties(serverDir, 25565)
    }
}

val writeBootstrapServerProperties = tasks.register("writeBootstrapServerProperties") {
    group = "setup"
    description = "Writes server.properties for the host-side bootstrap server."
    dependsOn(prepareTask)

    val serverDir = testPluginDir.resolve(serverDirectory)

    onlyIf {
        requiresMohistBootstrap
    }

    doLast {
        writeServerProperties(serverDir, bootstrapServerPort)
    }
}

val mohistBootstrapTask = tasks.register("bootstrapMohist") {
    group = "verification"
    description = "Runs $targetName once when Mohist has not generated its runtime files yet."
    dependsOn(prepareTask, writeBootstrapServerProperties)
    usesService(mohistBootstrapSlot)

    val serverDir = testPluginDir.resolve(serverDirectory)
    val serverJar = serverDir.resolve(serverJarName)
    val requiredPaths = listOf(
        serverDir.resolve("libraries"),
        serverDir.resolve("world"),
    )
    val javaExecutable = javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(minecraftJavaVersion))
    }.map { it.executablePath.asFile }

    onlyIf {
        requiresMohistBootstrap && requiredPaths.any { !it.exists() }
    }

    doLast {
        if (!serverJar.isFile) {
            throw GradleException("Mohist server jar does not exist: ${serverJar.absolutePath}")
        }

        serverDir.mkdirs()
        serverDir.resolve("eula.txt").writeText("eula=true\n")
        serverDir.resolve("mohist-config").mkdirs()
        serverDir.resolve("mohist-config/mohist.yml").writeText(
            """
            mohist:
              lang: en_US
              check_update: false
              check_update_auto_download: false
                libraries:
                  check: true
            """.trimIndent() + "\n"
        )

        val process = ProcessBuilder(
            javaExecutable.get().absolutePath,
            "-jar",
            serverJar.name,
            "nogui",
        )
            .directory(serverDir)
            .redirectErrorStream(true)
            .start()

        val output = StringBuilder()
        val ready = AtomicBoolean(false)
        val reader = process.inputStream.bufferedReader()
        val outputThread = Thread {
            reader.useLines { lines ->
                lines.forEach { line ->
                    output.appendLine(line)
                    logger.lifecycle("[$targetName] $line")
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
        while (process.isAlive && !ready.get() && System.nanoTime() - started < timeoutNanos) {
            Thread.sleep(500)
        }

        if (!ready.get()) {
            process.destroy()
            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly()
            }
            throw GradleException("Timed out while bootstrapping $targetName. Last output:\n$output")
        }

        process.outputStream.bufferedWriter().use {
            it.write("stop")
            it.newLine()
            it.flush()
        }

        if (!process.waitFor(2, TimeUnit.MINUTES)) {
            process.destroyForcibly()
            throw GradleException("Mohist bootstrap did not stop cleanly for $targetName.")
        }
        outputThread.join(TimeUnit.SECONDS.toMillis(10))

        val missingPaths = requiredPaths.filter { !it.exists() }
        if (missingPaths.isNotEmpty()) {
            throw GradleException(
                "Mohist bootstrap finished but required files are still missing: "
                        + missingPaths.joinToString { it.absolutePath }
            )
        }
    }
}

if (requiresMohistBootstrap) {
    writeDockerServerProperties.configure {
        mustRunAfter(mohistBootstrapTask)
    }
}

tasks.register<Test>("minecraftIntegrationTest") {
    group = "verification"
    description = "Runs the Docker-based Minecraft integration test for $minecraftVersion."
    usesService(minecraftIntegrationSlot)
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = files(sourceSets["test"].runtimeClasspath, mcProtocol)
    useJUnitPlatform()
    filter {
        includeTestsMatching("net.kunmc.lab.commandlib.integration.BukkitIntegrationTest")
    }
    testLogging {
        events("started", "passed", "skipped", "failed", "standardOut", "standardError")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
    dependsOn(writeDockerServerProperties)
    if (requiresMohistBootstrap) {
        dependsOn(mohistBootstrapTask)
    }
    systemProperty("commandlib.rootDir", rootProject.projectDir.absolutePath)
    systemProperty("commandlib.integrationTestDir", integrationTestDir.absolutePath)
    systemProperty("commandlib.testPluginDir", testPluginDir.absolutePath)
    systemProperty("commandlib.sharedDir", sharedDir.absolutePath)
    systemProperty("commandlib.runMinecraftIntegration", "true")
    systemProperty("commandlib.targetName", targetName)
    systemProperty("commandlib.platform", targetPlatform)
    systemProperty("commandlib.reportFileName", reportFileName)
    systemProperty("commandlib.serverDirectory", serverDirectory)
    systemProperty("commandlib.serverJarName", serverJarName)
    systemProperty("commandlib.minecraftJavaVersion", minecraftJavaVersion.toString())
}
