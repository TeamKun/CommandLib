extra["bukkitApiDependency"] = "io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT"
extra["javaVersion"] = "21"
extra["platform"] = "paper"
extra["minecraftServerVersion"] = "1.20.6"
extra["serverJarDownloads"] =
    "https://api.papermc.io/v2/projects/paper/versions/1.20.6/builds/81/downloads/paper-1.20.6-81.jar=>server/server.jar"

apply(from = "../test-plugin.shared.gradle.kts")
