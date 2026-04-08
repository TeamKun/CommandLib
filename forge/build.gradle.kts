buildscript {
    repositories {
        maven { url = uri("https://files.minecraftforge.net/maven") }
        mavenCentral()
    }
    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "5.1.+") {
            isChanging = true
        }
    }
}

apply(plugin = "net.minecraftforge.gradle")

repositories {
}

configure<net.minecraftforge.gradle.userdev.UserDevExtension> {
    mappings("snapshot", "20210309-1.16.5")
}

configurations {
    create("adder")
}

dependencies {
    api(project(":common"))
    "minecraft"("net.minecraftforge:forge:1.16.5-36.2.20")
    implementation("org.jetbrains:annotations:16.0.2")
}
