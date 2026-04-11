extra["bukkitApiDependency"] = "io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT"
extra["javaVersion"] = "17"
extra["platform"] = "paper"
extra["minecraftServerVersion"] = "1.19.4"
extra["serverJarDownloads"] =
    "https://api.papermc.io/v2/projects/paper/versions/1.19.4/builds/538/downloads/paper-1.19.4-538.jar=>server/server.jar"

apply(from = "../test-plugin.shared.gradle.kts")
