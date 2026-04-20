extra["bukkitApiDependency"] = "io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT"
extra["javaVersion"] = "21"
extra["platform"] = "paper"
extra["minecraftServerVersion"] = "1.20.5"
extra["serverJarDownloads"] =
    "https://api.papermc.io/v2/projects/paper/versions/1.20.5/builds/22/downloads/paper-1.20.5-22.jar=>server/server.jar"

apply(from = "../test-plugin.shared.gradle.kts")
