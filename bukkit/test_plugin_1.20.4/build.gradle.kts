import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

plugins {
    java
    id("com.gradleup.shadow") version "9.4.1"
}

group = "net.kunmc.lab"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    flatDir { dirs("server/cache", "libs") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.mojang:brigadier:1.0.18")
    implementation("com.opencsv:opencsv:5.9")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
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

sourceSets {
    main {
        java {
            srcDirs("../src/main/java", "../../common/src/main/java", "../test_plugin_common/src/main/java")
        }
        resources {
            srcDirs("../src/main/resources")
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
tasks.named("build") { dependsOn(tasks.named("shadowJar")) }
tasks.named("jar") { finalizedBy(tasks.named("shadowJar")) }

tasks.named<ProcessResources>("processResources") {
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
        copy {
            from(File(layout.buildDirectory.get().asFile.absolutePath, "libs/${rootProject.name}-${version}.jar"))
            into("./server/plugins")
        }
    }
}

tasks.register("buildAndCopy") {
    group = "build"
    dependsOn("build", "copyToServer")
}

tasks.register("downloadServerJar") {
    val buildNumber = "496"
    val url =
        URL("https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/$buildNumber/downloads/paper-1.20.4-$buildNumber.jar")
    val file = File(projectDir.toPath().toAbsolutePath().toString() + "/server/server.jar")
    if (!file.exists()) {
        url.openStream().use { it.copyTo(file.outputStream()) }
    }
}

fun getMainClassFQDN(projectPath: Path): String {
    val mainClassFile = Files.walk(projectPath)
        .filter { it.fileName.toString().endsWith(".java") }
        .filter { Files.lines(it).anyMatch { str -> str.contains("extends JavaPlugin") } }
        .findFirst()
        .get()
    return mainClassFile.toString()
        .replace("\\", ".")
        .replace("/", ".")
        .replace(Regex(".*src.main.java.|.java$"), "")
}
