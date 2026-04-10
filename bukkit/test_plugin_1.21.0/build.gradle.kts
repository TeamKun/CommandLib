extra["paperApiDependency"] = "io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT"
extra["javaVersion"] = "21"
extra["serverJarDownloads"] =
    "https://api.papermc.io/v2/projects/paper/versions/1.21/builds/130/downloads/paper-1.21-130.jar=>server/server.jar"

apply(from = "../test-plugin.shared.gradle.kts")
