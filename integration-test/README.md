# Bukkit Integration Test

## Purpose

This module automates the manual Test Plugin workflow.
The main goal is to verify that commands using each `Argument` can be registered, parsed, and executed on a real
Minecraft server.

This is intentionally not a normal unit test.
CommandLib depends on Bukkit, Brigadier, and NMS behavior that is only reliable inside an actual server runtime.

Fast tests remain in `spigot/src/test` and `spigot-testing`.
Use this module only for cases that need a real Bukkit-compatible server, NMS, command registration, or a real player
connection.

## Flow

Each `minecraftIntegrationTest*` task does the following:

1. Builds the target fixture plugin.
2. Downloads or prepares the matching server jar and server-side helper plugin jars.
3. Starts the server in Docker through Testcontainers.
4. Connects a fake player named `Maru32768` through MCProtocolLib.
5. Lets the plugin execute its command test suite in game.
6. Waits for the plugin to write a JUnit XML report.
7. Fails the Gradle test if the JUnit XML report contains failures or errors.

The report is written under:

```text
integration-test/shared/test-results/
```

## Why MCProtocolLib

The plugin cases are command execution tests, not just parser unit tests.
A real player connection exercises the Bukkit command path more closely than a mocked sender.

MCProtocolLib is used because it can connect to the server without launching a full Minecraft client.
Different Minecraft versions require different MCProtocolLib artifacts, so Gradle keeps each protocol client on its own
configuration.

## Why Docker

The server process needs version-specific Minecraft and Bukkit-compatible server classes.
Running it in Docker keeps the Java runtime and server process isolated from the Gradle test JVM.

The integration test intentionally fails when Docker is unavailable.
These tasks are opt-in and are expected to validate a real server, so a missing Docker daemon should be treated as an
infrastructure failure rather than a skipped test.

## Layout

Each target owns its test plugin project:

```text
integration-test/targets/{platform}-{version}/test-plugin/
```

Shared test plugin code lives under:

```text
integration-test/shared/bukkit-test-plugin
```

The shared Gradle script is:

```text
integration-test/shared/bukkit-test-plugin.gradle.kts
```

Target plugin setup downloads PlugManX and AutoReloader into each configured server `plugins` directory.
The AutoReloader URL can be overridden when needed:

```powershell
.\gradlew.bat :integration-test:targets:paper-1.20.4:prepareTestPlugin -PautoReloaderJarDownloadUrl=<download-url>
```

The same URL can be supplied with `COMMANDLIB_AUTORELOADER_JAR_URL`.

## Targets

Each version has a prepare task and an integration test task.
For example:

```text
:integration-test:targets:paper-1.20.4:prepareTestPlugin
:integration-test:targets:paper-1.20.4:minecraftIntegrationTest
```

`minecraftIntegrationTest` is the aggregate task for all configured targets.
The aggregate task depends on per-target subprojects, so Gradle can run independent targets in parallel by default.

## Generated NMS Jars

Some tests need a real server jar that contains NMS and CraftBukkit classes.
The downloadable server launcher jar is not always that jar.

Target plugin projects expose `generatePatchedJar` for this purpose.
The task starts the downloaded server once when the expected generated jar is missing, then validates that the
distribution-specific jar path exists.

Paper has two known layouts:

- Paper `1.17.2` and older: `server/cache/patched_<version>.jar`
- Paper `1.18` and newer: `server/versions/<version>/paper-<version>.jar`

Mohist does not follow Paper's single-path convention consistently.
When a Mohist target needs generated NMS jars, configure `nmsJarPaths` explicitly in that target plugin's `build.gradle.kts`.
For example, Mohist `1.20.1` splits server and CraftBukkit classes across Forge-generated jars, so the fixture checks
both generated jar paths.

The `spigot` module's NMS resolver tests can optionally load a generated jar.
The default fallback path for the legacy resolver test is:

```text
integration-test/targets/paper-1.16.5/test-plugin/server/cache/patched_1.16.5.jar
```

`COMMANDLIB_NMS_TEST_JAR_1_16_5` can override that path.

## Mohist Constraint

Mohist targets require a first startup before the integration test server is launched in Docker.
That startup generates Mohist libraries, mappings, and server-side files in the target plugin's `server` directory.

Gradle handles this with per-target `bootstrapMohist` tasks.
The task checks for generated marker paths such as `libraries` and `world`.
If they are missing, it starts Mohist once with the target Java toolchain, waits until the server reaches the ready
state, sends `stop`, and then lets the Docker-based integration test continue.

Example:

```powershell
.\gradlew.bat :integration-test:targets:mohist-1.20.1:prepareTestPlugin
.\gradlew.bat :integration-test:targets:mohist-1.20.1:bootstrapMohist
```

The versioned integration test task depends on this bootstrap task, so manual execution is only needed when diagnosing
Mohist startup itself.

## Running

Run one target:

```powershell
.\gradlew.bat :integration-test:targets:paper-1.20.4:minecraftIntegrationTest
```

Run all configured targets:

```powershell
.\gradlew.bat :integration-test:minecraftIntegrationTest
```

Gradle project parallelism is enabled by default, but Minecraft server tests are throttled to avoid starting every
Docker server at once. The default server-test concurrency is 4.

For smaller Docker Desktop environments, reduce it:

```powershell
.\gradlew.bat :integration-test:minecraftIntegrationTest -Pcommandlib.minecraftIntegrationMaxParallel=1
```

Mohist bootstrap is throttled separately and defaults to 2. Each Mohist target writes a target-specific bootstrap
`server-port`, so bootstrap tasks can run in parallel when the local machine has enough CPU and memory.

To reduce Mohist bootstrap concurrency:

```powershell
.\gradlew.bat :integration-test:minecraftIntegrationTest -Pcommandlib.mohistBootstrapMaxParallel=1
```

The normal `test` task runs lightweight integration coverage checks only. Real server tests run from the target
subprojects' `minecraftIntegrationTest` tasks.

## Caveats

- Docker availability depends on the local Docker Desktop environment.
- Docker-based runs can be environment-sensitive even when normal `test` passes.
- MCProtocolLib compatibility should be validated on the real target environment when adding or upgrading server
  versions.
- Mohist startup is intentionally split into `bootstrapMohist` because Mohist generates libraries, mappings, and
  server-side files during startup.
