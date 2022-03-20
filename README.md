## Getting Started

### Gradle

```groovy
plugins {
    id "com.github.johnrengelman.shadow" version "6.1.0"
}

configurations {
    adder
}

dependencies {
    compileOnly ""
    adder ""
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