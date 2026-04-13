# Testing Notes

Use focused Gradle tasks for the modules affected by the change.

Common checks:

```bash
./gradlew :common:test
./gradlew :bukkit:test
./gradlew :bukkit-test:test
```

Compile checks:

```bash
./gradlew :common:compileJava
./gradlew :bukkit:compileJava
./gradlew :bukkit-test:compileJava
```

Integration tests may require Docker or a local Minecraft/Bukkit test
environment. Run them only when the touched behavior requires it or the user
asks for it:

```bash
./gradlew :bukkit-integration-test:minecraftIntegrationTest
```

When adding or fixing command behavior, prefer narrow tests around parsing,
execution, options, permissions, and suggestions. Use `bukkit-test` utilities
for command-level Bukkit tests when a full server is not needed.
