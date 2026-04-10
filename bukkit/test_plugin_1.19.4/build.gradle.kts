extra["paperApiDependency"] = "io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT"
extra["javaVersion"] = "17"
extra["copyTargets"] = "server/plugins|server_mojmap/plugins"
extra["serverJarDownloads"] =
    "https://api.papermc.io/v2/projects/paper/versions/1.19.4/builds/538/downloads/paper-1.19.4-538.jar=>server/server.jar|" +
        "https://api.papermc.io/v2/projects/paper/versions/1.19.4/builds/538/downloads/paper-mojmap-1.19.4-538.jar=>server_mojmap/server.jar"

apply(from = "../test-plugin.shared.gradle.kts")
