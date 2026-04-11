import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

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

configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

// MCProtocolLib has incompatible package names and transitive dependencies across Minecraft versions.
// Keep each version on an isolated configuration so one integration target cannot accidentally resolve
// another target's protocol client.
val mcProtocol1204 = configurations.create("mcProtocol1204")
val mcProtocol1206 = configurations.create("mcProtocol1206")
val mcProtocol121 = configurations.create("mcProtocol121")
val mcProtocol1165 = configurations.create("mcProtocol1165")
val mcProtocol1194 = configurations.create("mcProtocol1194")
val mcProtocol1201 = configurations.create("mcProtocol1201")

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.awaitility:awaitility:4.2.1")
    testImplementation("org.testcontainers:junit-jupiter:1.21.4")
    testImplementation("org.testcontainers:testcontainers:1.21.4")
    testRuntimeOnly("org.slf4j:slf4j-simple:1.7.36")
    testImplementation("net.kyori:adventure-text-serializer-gson:4.15.0")
    testImplementation("net.kyori:adventure-text-serializer-json-legacy-impl:4.15.0")

    mcProtocol1165("com.github.steveice10:mcprotocollib:1.16.5-1")
    mcProtocol1194("com.github.steveice10:mcprotocollib:1.19.4-1")
    mcProtocol1201("com.github.steveice10:mcprotocollib:1.20-1")
    mcProtocol1204("com.github.steveice10:mcprotocollib:1.20.4-1") {
        exclude(group = "net.kyori", module = "adventure-text-serializer-gson")
        exclude(group = "net.kyori", module = "adventure-text-serializer-json-legacy-impl")
    }
    mcProtocol1206("org.geysermc.mcprotocollib:protocol:1.20.6-2-SNAPSHOT")
    mcProtocol121("com.github.steveice10:mcprotocollib:1.21-1")
}

data class IntegrationTarget(
    val id: String,
    val fixtureName: String,
    val minecraftVersion: String,
    val reportFileName: String,
    val javaVersion: Int,
    val protocolConfiguration: Configuration,
    val serverDirectory: String = "server",
    val serverJarName: String = "server.jar",
    val requiresMohistBootstrap: Boolean = false,
)

val integrationTargets = listOf(
    IntegrationTarget(
        id = "1165",
        fixtureName = "test-plugin-1.16.5",
        minecraftVersion = "1.16.5",
        reportFileName = "TEST-commandlib-1.16.5.xml",
        javaVersion = 11,
        protocolConfiguration = mcProtocol1165,
    ),
    IntegrationTarget(
        id = "1165Mohist",
        fixtureName = "test-plugin-1.16.5-mohist",
        minecraftVersion = "1.16.5",
        reportFileName = "TEST-commandlib-1.16.5-mohist.xml",
        javaVersion = 17,
        protocolConfiguration = mcProtocol1165,
        serverJarName = "mohist.jar",
        requiresMohistBootstrap = true,
    ),
    IntegrationTarget(
        id = "1194",
        fixtureName = "test-plugin-1.19.4",
        minecraftVersion = "1.19.4",
        reportFileName = "TEST-commandlib-1.19.4.xml",
        javaVersion = 17,
        protocolConfiguration = mcProtocol1194,
    ),
    IntegrationTarget(
        id = "1201",
        fixtureName = "test-plugin-1.20.1",
        minecraftVersion = "1.20.1",
        reportFileName = "TEST-commandlib-1.20.1.xml",
        javaVersion = 17,
        protocolConfiguration = mcProtocol1201,
    ),
    IntegrationTarget(
        id = "1201Mohist",
        fixtureName = "test-plugin-1.20.1-mohist",
        minecraftVersion = "1.20.1",
        reportFileName = "TEST-commandlib-1.20.1-mohist.xml",
        javaVersion = 17,
        protocolConfiguration = mcProtocol1201,
        serverJarName = "mohist.jar",
        requiresMohistBootstrap = true,
    ),
    IntegrationTarget(
        id = "1204",
        fixtureName = "test-plugin-1.20.4",
        minecraftVersion = "1.20.4",
        reportFileName = "TEST-commandlib-1.20.4.xml",
        javaVersion = 17,
        protocolConfiguration = mcProtocol1204,
    ),
    IntegrationTarget(
        id = "1205",
        fixtureName = "test-plugin-1.20.5",
        minecraftVersion = "1.20.5",
        reportFileName = "TEST-commandlib-1.20.5.xml",
        javaVersion = 21,
        protocolConfiguration = mcProtocol1206,
    ),
    IntegrationTarget(
        id = "1206",
        fixtureName = "test-plugin-1.20.6",
        minecraftVersion = "1.20.6",
        reportFileName = "TEST-commandlib-1.20.6.xml",
        javaVersion = 21,
        protocolConfiguration = mcProtocol1206,
    ),
    IntegrationTarget(
        id = "1210",
        fixtureName = "test-plugin-1.21.0",
        minecraftVersion = "1.21.0",
        reportFileName = "TEST-commandlib-1.21.xml",
        javaVersion = 21,
        protocolConfiguration = mcProtocol121,
    ),
)

val runMinecraftIntegration = providers.gradleProperty("runMinecraftIntegration")
    .orElse(providers.environmentVariable("COMMANDLIB_RUN_MINECRAFT_IT"))
    .map { it.equals("true", ignoreCase = true) }
    .orElse(false)
// Fixture builds are nested Gradle builds. A dedicated Gradle user home prevents those wrapper builds from
// polluting the repository root Gradle cache and makes their downloaded server jars easier to reason about.
val nestedGradleUserHome = rootProject.file(".gradle-user-home-bukkit-it")
val fixturesDir = project.file("fixtures")
val isWindows = System.getProperty("os.name").startsWith("Windows", ignoreCase = true)
val javaToolchains = extensions.getByType<JavaToolchainService>()
// Only the target protocol configuration should contribute MCProtocolLib. Multiple versions on the same
// runtime classpath make the reflection-based client creation ambiguous.
val baseTestRuntimeClasspath = sourceSets["test"].runtimeClasspath.filter {
    !it.name.startsWith("mcprotocollib-", ignoreCase = true)
}

fun IntegrationTarget.capitalizedId(): String = id.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase() else it.toString()
}

fun registerPrepareTask(target: IntegrationTarget): TaskProvider<Exec> =
    tasks.register<Exec>("prepareTestPlugin${target.capitalizedId()}") {
        val testPluginDir = fixturesDir.resolve(target.fixtureName)
        val hasWrapperJar = testPluginDir.resolve("gradle/wrapper/gradle-wrapper.jar").isFile

        // Most fixtures can be built from the root wrapper, but some historical fixtures carry their own
        // wrapper because their Gradle/plugin combination no longer behaves correctly under the root build.
        workingDir(if (hasWrapperJar) testPluginDir else rootProject.projectDir)
        environment("GRADLE_USER_HOME", nestedGradleUserHome.absolutePath)

        if (isWindows) {
            if (hasWrapperJar) {
                commandLine("cmd", "/c", "gradlew.bat", "buildAndCopy", "downloadServerJar")
            } else {
                commandLine(
                    "cmd",
                    "/c",
                    "gradlew.bat",
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

fun registerMohistBootstrapTask(target: IntegrationTarget, prepareTask: TaskProvider<Exec>): TaskProvider<Task> =
    tasks.register("bootstrapMohist${target.capitalizedId()}") {
        group = "verification"
        description = "Runs ${target.fixtureName} once when Mohist has not generated its runtime files yet."
        dependsOn(prepareTask)

        val serverDir = fixturesDir.resolve(target.fixtureName).resolve(target.serverDirectory)
        val serverJar = serverDir.resolve(target.serverJarName)
        val requiredPaths = listOf(
            serverDir.resolve("libraries"),
            serverDir.resolve("world"),
        )
        val javaExecutable = javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(target.javaVersion))
        }.map { it.executablePath.asFile }

        // Mohist downloads libraries and creates mappings/world files during its first server startup.
        // Running that slow bootstrap inside the actual Testcontainers test hides the real failure point, so
        // this task makes the precondition explicit and reuses the generated fixture on later runs.
        onlyIf {
            target.requiresMohistBootstrap && requiredPaths.any { !it.exists() }
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
            serverDir.resolve("server.properties").writeText(
                """
                online-mode=false
                server-port=25565
                enforce-secure-profile=false
                motd=CommandLib IT
                gamemode=creative
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
                        logger.lifecycle("[${target.fixtureName}] $line")
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
                throw GradleException("Timed out while bootstrapping ${target.fixtureName}. Last output:\n$output")
            }

            process.outputStream.bufferedWriter().use {
                it.write("stop")
                it.newLine()
                it.flush()
            }

            if (!process.waitFor(2, TimeUnit.MINUTES)) {
                process.destroyForcibly()
                throw GradleException("Mohist bootstrap did not stop cleanly for ${target.fixtureName}.")
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

fun registerVersionedIntegrationTest(target: IntegrationTarget): TaskProvider<Test> {
    val prepareTask = registerPrepareTask(target)
    val mohistBootstrapTask = if (target.requiresMohistBootstrap) registerMohistBootstrapTask(target, prepareTask) else null

    return tasks.register<Test>("minecraftIntegrationTest${target.capitalizedId()}") {
        group = "verification"
        description = "Runs the Docker-based Minecraft integration test for ${target.minecraftVersion}."
        testClassesDirs = sourceSets["test"].output.classesDirs
        classpath = files(baseTestRuntimeClasspath, target.protocolConfiguration)
        useJUnitPlatform()
        dependsOn(mohistBootstrapTask ?: prepareTask)
        // The Java test is shared by all versions; system properties keep the version matrix in Gradle where
        // dependency and fixture selection already live.
        systemProperty("commandlib.rootDir", rootProject.projectDir.absolutePath)
        systemProperty("commandlib.fixturesDir", fixturesDir.absolutePath)
        systemProperty("commandlib.runMinecraftIntegration", "true")
        systemProperty("commandlib.fixtureName", target.fixtureName)
        systemProperty("commandlib.reportFileName", target.reportFileName)
        systemProperty("commandlib.serverDirectory", target.serverDirectory)
        systemProperty("commandlib.serverJarName", target.serverJarName)
        systemProperty("commandlib.minecraftJavaVersion", target.javaVersion.toString())
    }
}

val versionedIntegrationTasks = integrationTargets.associateWith(::registerVersionedIntegrationTest)

// IntelliJ asks every Gradle project for this task while importing Kotlin DSL projects. Defining the no-op
// task keeps the integration-test module importable even though it is not a normal application module.
tasks.register("prepareKotlinBuildScriptModel")

tasks.test {
    useJUnitPlatform()
    // Real-server tests are intentionally opt-in. They need Docker and Minecraft server jars, so running them
    // from the normal unit-test task would make local development and CI checks unexpectedly slow/flaky.
    onlyIf { runMinecraftIntegration.get() }
    systemProperty("commandlib.rootDir", rootProject.projectDir.absolutePath)
    systemProperty("commandlib.fixturesDir", fixturesDir.absolutePath)
    systemProperty("commandlib.runMinecraftIntegration", runMinecraftIntegration.get().toString())
}

tasks.register("minecraftIntegrationTest") {
    group = "verification"
    description = "Runs all Docker-based Minecraft integration tests."
    dependsOn(versionedIntegrationTasks.values)
}
