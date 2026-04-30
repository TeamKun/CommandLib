pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "CommandLib"
include("forge", "spigot", "paper")
include("common")
include("spigot-testing", "paper-testing")
include("integration-test")
