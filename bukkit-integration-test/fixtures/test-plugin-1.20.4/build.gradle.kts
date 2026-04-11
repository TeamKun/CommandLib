extra["bukkitApiDependency"] = "io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT"
extra["javaVersion"] = "17"
extra["platform"] = "paper"
extra["minecraftServerVersion"] = "1.20.4"
extra["serverJarDownloads"] =
    "https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/496/downloads/paper-1.20.4-496.jar=>server/server.jar"

apply(from = "../test-plugin.shared.gradle.kts")
