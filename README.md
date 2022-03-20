## Getting Started

### Gradle Settings

```groovy
plugins {
    id "com.github.johnrengelman.shadow" version "6.1.0"
}

configurations {
    adder
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly "com.github.TeamKun:CommandLib:latest.release"
    adder "com.github.TeamKun:CommandLib:latest.release"
}

shadowJar {
    configurations = [project.configurations.adder]
    relocate "net.kunmc.lab.commandlib", "${project.group}.${project.name.toLowerCase()}.commandlib"
    finalizedBy("reobfShadowJar")
}

reobf {
    shadowJar {
    }
}
```