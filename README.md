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
    finalizedBy("reobfShadowJar")
}

reobf {
    shadowJar {
    }
}
```