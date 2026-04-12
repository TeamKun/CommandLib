# CommandLib - A Feature-Rich Minecraft Command API for Bukkit

[![](https://jitpack.io/v/Maru32768/CommandLib.svg)](https://jitpack.io/#Maru32768/CommandLib)

CommandLib is an advanced, type-safe Command API for Bukkit(Spigot/Paper) developers. It abstracts and enhances the
Minecraft 1.13
command APIs, simplifying command implementation. With features like customizable argument suggestions, seamless
validation, improved usability, and extensibility, CommandLib empowers developers to build better commands with ease.

#### Supported Versions

| Platform                 | Tested Versions                                            | Notes                                              |
|--------------------------|------------------------------------------------------------|----------------------------------------------------|
| **Bukkit(Spigot/Paper)** | `1.16.5`, `1.19.4`, `1.20.1`, `1.20.4`, `1.20.6`, `1.21.0` | Expected to work on intermediate versions.         |
| **Forge**                | `1.16.5`                                                   | Currently supports only `1.16.5` and works fully.  |
| **Mohist**               | `1.16.5`, `1.20.1`                                         | Works on Mohist since it's compatible with Spigot. |

**Requires Java 11 or later.**

## Features

1. **Type-Safe Arguments Handling**  
   Use arguments in a type-safe manner directly within your code, reducing potential runtime errors and improving
   maintainability.
2. **No Need for Brigadier and NMS Dependency**  
   Simplify your setup and ensure compatibility across multiple Minecraft versions.
3. **Powerful Suggestion Generation**  
   Automatically generate argument suggestions with customizable options for enhanced flexibility.
4. **Typed Command Options**  
   Define command options such as `-f`, `--force`, or `--limit 10` and read them from the command context in a
   type-safe way.
5. **Seamless Integration with the `/execute` command**   
   Allow your commands to be executed seamlessly from the `/execute` command, just like built-in commands.

## Installation

To ensure stability, we recommend replacing `latest.release` with a specific version such as `0.16.0`.  
You can find the latest version on the [releases page](https://github.com/Maru32768/CommandLib/releases).

<details>
<summary>Bukkit (Groovy DSL)</summary>

```groovy
plugins {
    id "com.gradleup.shadow" version "8.3.5"
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation "com.github.Maru32768.CommandLib:bukkit:latest.release"
}

shadowJar {
    archiveFileName = "${rootProject.name}-${project.version}.jar"
    // Avoid package conflicts
    relocate "net.kunmc.lab.commandlib", "${project.group}.${project.name.toLowerCase()}.commandlib"
}
tasks.build.dependsOn tasks.shadowJar
```

</details>

<details>
<summary>Bukkit (Kotlin DSL)</summary>

```kotlin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.Maru32768.CommandLib:bukkit:latest.release")
}

val projectGroup = project.group.toString()
val projectNameLower = project.name.lowercase()
tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("${rootProject.name}-${project.version}.jar")
    // Avoid package conflicts
    relocate("net.kunmc.lab.commandlib", "$projectGroup.$projectNameLower.commandlib")
}
tasks.named("build") { dependsOn(tasks.named("shadowJar")) }
```

</details>

<details>
<summary>Forge (Groovy DSL)</summary>

```groovy
plugins {
    id "com.gradleup.shadow" version "8.3.5"
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation "com.github.Maru32768.CommandLib:forge:latest.release"
}

shadowJar {
    archiveFileName = "${rootProject.name}-${project.version}.jar"
    dependencies {
        include(dependency("com.github.Maru32768.CommandLib:forge:.*"))
    }
    // Avoid package conflicts
    relocate "net.kunmc.lab.commandlib", "${project.group}.${project.name.toLowerCase()}.commandlib"
    finalizedBy("reobfShadowJar")
}

reobf {
    shadowJar {
    }
}
```

</details>

<details>
<summary>Forge (Kotlin DSL)</summary>

```kotlin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.Maru32768.CommandLib:forge:latest.release")
}

val projectGroup = project.group.toString()
val projectNameLower = project.name.lowercase()
tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("${rootProject.name}-${project.version}.jar")
    dependencies {
        include(dependency("com.github.Maru32768.CommandLib:forge:.*"))
    }
    // Avoid package conflicts
    relocate("net.kunmc.lab.commandlib", "$projectGroup.$projectNameLower.commandlib")
    finalizedBy("reobfShadowJar")
}

@Suppress("UNCHECKED_CAST")
(extensions.getByName("reobf") as NamedDomainObjectContainer<Any>).create("shadowJar")
```

</details>

## Code Examples

<details>
<summary>Defining Commands</summary>

```java
public final class TestPlugin extends JavaPlugin {
    public void onEnable() {
        CommandLib.register(this, new Command("message") {{
            argument(new PlayerArgument("target"), new StringArgument("message"), (target, message, ctx) -> {
                // 'target' is inferred as 'org.bukkit.entity.Player'. No need to cast.
                target.sendMessage(message);
            });
        }});
    }
}
```

```java
// Also you can define commands by extending Command
public final class MessageCommand extends Command {
    public MessageCommand() {
        super("message");
        argument(new PlayerArgument("target"), new StringArgument("message"), (target, message, ctx) -> {
            target.sendMessage(message);
        });
    }
}

public final class TestPlugin extends JavaPlugin {
    public void onEnable() {
        CommandLib.register(this, new MessageCommand());
    }
}
```

</details>

<details>
<summary>Appending Subcommands</summary>

```java
public final class TestPlugin extends JavaPlugin {
    public void onEnable() {
        CommandLib.register(this, new Command("game") {{
            addChildren(new Command("start") {{
                execute(ctx -> {
                    // Starts game
                });
            }}, new Command("stop") {{
                execute(ctx -> {
                    // Stops game
                });
            }});
        }});
    }
}
```

</details>

<details>
<summary>Defining Command Options</summary>

Command options can be placed immediately after the command name and before regular arguments.

```java
public final class ScanCommand extends Command {
    public ScanCommand() {
        super("scan");

        CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f')
                                                                     .description("Run even if safety checks fail."));
        CommandOption<Boolean, CommandContext> verbose = option(Options.flag("verbose", 'v')
                                                                       .description("Send detailed progress messages."));
        CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100)
                                                                     .description("Maximum number of targets."));

        argument(new PlayerArgument("target"), (target, ctx) -> {
            boolean isForce = ctx.getOption(force);
            boolean isVerbose = ctx.getOption(verbose);
            int maxCount = ctx.getOption(limit);

            // Do something
        });
    }
}
```

Valid inputs:

```text
/scan Steve
/scan -f Steve
/scan --force Steve
/scan -f -v Steve
/scan -fv Steve
/scan -n 20 Steve
/scan --limit 20 Steve
/scan -fv -n 20 Steve
```

Options must appear before regular arguments. This is invalid:

```text
/scan Steve -f
```

For subcommands, options belong to the most specific child command and are placed after that child command name:

```java
public final class GameCommand extends Command {
    public GameCommand() {
        super("game");

        addChildren(new Command("start") {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));

            argument(new StringArgument("arena"), (arena, ctx) -> {
                boolean isForce = ctx.getOption(force);

                // Starts game
            });
        }});
    }
}
```

Valid:

```text
/game start -f arena1
```

Invalid:

```text
/game -f start arena1
```

Supported option types:

```java
Options.flag("force", 'f')             // boolean flag, false when omitted
Options.bool("enabled", 'e', true)
Options.integer("limit", 'n', 10)
Options.longValue("size", 's', 0L)
Options.floatValue("speed", 'S', 1.0f)
Options.doubleValue("radius", 'r', 5.0)
Options.string("format", 'F', "text")
```

Value options use the separated form, such as `--limit 20` or `-n 20`. Forms like `--limit=20` and `-n20` are not
supported.

Option descriptions are optional. When present, they are shown in the command help message.

</details>


<details>
<summary>Suggesting Block Materials</summary>

```java
public final class TestPlugin extends JavaPlugin {
    public void onEnable() {
        CommandLib.register(this, new Command("test") {{
            argument(new EnumArgument<>("block", Material.class, option -> {
                option.filter(x -> {
                    if (!x.isBlock()) {
                        // Displays an error message to the sender if the argument is not a block material.
                        throw new InvalidArgumentException(x.name() + " is not block.");
                    }
                });
            }), (m, ctx) -> {
                // Do something
            });
        }});
    }
}
```

</details>

<details>
<summary>Defining Variable Length Arguments</summary>

```java
public final class TestPlugin extends JavaPlugin {
    public void onEnable() {
        CommandLib.register(this, new Command("test") {{
            argument(new PlayerArgument("player"), (player, ctx) -> {
                // Do something
            });
            argument(new PlayerArgument("player"), new StringArgument("message"), (player, message, ctx) -> {
                // Do something
            });
        }});
    }
}
```

</details>

## Permissions (Bukkit)

CommandLib automatically generates and registers Bukkit permission nodes for each command.

### Permission Prefix

By default, permission nodes are generated as `minecraft.command.<name>`.  
Pass a custom prefix to `CommandLib.register()` to use your own namespace:

```java
public final class TestPlugin extends JavaPlugins {
    @Override
    public void onEnable() {
        // Generates "myplugin.command.spawn", "myplugin.command.game.start", etc.
        CommandLib.register(this, "myplugin.command", new SpawnCommand(), new GameCommand());
    }
}
```

The prefix is applied to all commands and their subcommands recursively.

### Custom Permission Node

To assign a specific permission node to a command instead of the auto-generated one:

```java
public final class SpawnCommand extends Command {
    public SpawnCommand() {
        super("spawn");
        permission("myplugin.admin");           // fixed node, ignores prefix
        permission("myplugin.admin", PermissionDefault.FALSE); // with default
    }
}
```

### Permission Default

Control who has the permission by default:

```java
public final class TestCommand extends Command {
    public TestCommand() {
        super("test");

        permission(PermissionDefault.OP);    // default - only operators

        permission(PermissionDefault.TRUE);  // everyone

        permission(PermissionDefault.FALSE); // no one (must be granted explicitly)
    }
}
```

### LuckPerms Compatibility

CommandLib uses Bukkit's standard `sender.hasPermission()` for all permission checks.  
LuckPerms integrates with Bukkit's permission system, so **no additional configuration is needed** — LuckPerms
permissions work out of the box.

## Claude Code Skill

A Claude Code skill is available at `.claude/skills/commandlib/`. It loads the CommandLib API from your Gradle source
cache and generates or explains code on demand.

### Setup

1. Copy the `.claude/skills/commandlib/` directory to your project's `.claude/skills/` directory.

2. Download sources via Gradle so the skill has something to read:

```bash
./gradlew dependencies --configuration compileClasspath
```

Or in IntelliJ: **View > Tool Windows > Gradle > Download Sources**.

### Usage

```
/commandlib <request>
```

```
/commandlib I want a command that sends a message to a player. Args: player and string. Permission: myplugin.message
/commandlib プレイヤーにメッセージを送るコマンドを作りたい。引数はプレイヤーと文字列、権限は myplugin.message
```

## Sample Projects

[Bukkit](./sample/bukkit)  
[Forge](./sample/forge)
