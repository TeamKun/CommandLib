allprojects {
    group = "net.kunmc.lab"
    version = "0.17.2"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "idea")

    extensions.configure<org.gradle.plugins.ide.idea.model.IdeaModel> {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(11))
        withSourcesJar()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()

                from(components["java"])
            }
        }
    }

    tasks.named<Jar>("jar") {
        doFirst {
            copy {
                from(project.rootDir.toPath().toAbsolutePath())
                into(layout.buildDirectory.dir("resources/main"))
                include("LICENSE*")
            }
        }
    }

    extensions.configure<JavaPluginExtension> {
        withSourcesJar()
    }

    tasks.named<Jar>("sourcesJar") {
        from(rootProject.file("README.md"))
    }
}
