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
5. **Automatic Usage and Help Generation**  
   Generate usage and help messages from the command tree, including arguments, subcommands, options, and descriptions.
6. **Seamless Integration with the `/execute` command**   
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
            argument(new PlayerArgument("target"), new StringArgument("message")).execute((target, message, ctx) -> {
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
        argument(new PlayerArgument("target"), new StringArgument("message")).execute((target, message, ctx) -> {
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
<summary>Appending Subcommands After Arguments</summary>

CommandLib also supports Brigadier-style command trees where an argument is followed by a literal subcommand.
Use this for vanilla-like command shapes such as `/config <key> get` or `/config <key> set <value>`.

For most plugin commands, prefer the simpler literal-first shape, such as `/config get <key>` and
`/config set <key> <value>`. Argument-first trees are useful when the target naturally comes before the action, or when
you need to match an existing command syntax.

```java
public final class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config");

        argument(new StringArgument("key")).description("Select a config key")
                                           .child(keyArg -> new Command("get") {{
                                               execute(ctx -> {
                                                   String key = ctx.getArgument(keyArg);

                                                   // Get config value
                                               });
                                           }})
                                           .child(keyArg -> new Command("set") {{
                                               argument(new StringArgument("value")).execute((valueValue, ctx) -> {
                                                   String key = ctx.getArgument(keyArg);

                                                   // Set config value
                                               });
                                           }});
    }
}
```

Valid inputs:

```text
/config difficulty get
/config difficulty set hard
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

        argument(new PlayerArgument("target")).execute((target, ctx) -> {
            boolean isForce = ctx.getOption(force);
            boolean isVerbose = ctx.getOption(verbose);
            int maxCount = ctx.getOption(limit);
            boolean limitWasSpecified = ctx.hasOption(limit);

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

            argument(new StringArgument("arena")).execute((arena, ctx) -> {
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

Value options use the separated form, such as `--limit 20` or `-n 20`. Forms like `--limit=20` and `-n20` are not
supported.

Option descriptions are optional. When present, they are shown in the command help message.

Use `ctx.hasOption(option)` when you need to distinguish an explicitly specified option from its default value.

Options can also depend on other options:

```java
public final class ExportCommand extends Command {
    public ExportCommand() {
        super("export");

        CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f')
                                                                     .description("Overwrite existing files."));
        CommandOption<String, CommandContext> reason = option(Options.string("reason", 'r', "")
                                                                     .description("Reason for forcing.")
                                                                     .requires(force));
        CommandOption<String, CommandContext> mode = option(Options.string("mode", 'm', "normal"));
        CommandOption<Integer, CommandContext> threads = option(Options.integer("threads", 't', 1, 1, 16)
                                                                       .requires(mode, "parallel"));

        argument(new StringArgument("target")).execute((target, ctx) -> {
            // --reason can be used only with --force.
            // --threads can be used only when ctx.getOption(mode) is parallel.
        });
    }
}
```

Valid:

```text
/export -f -r cleanup data
/export -t 4 -m parallel data
```

Invalid:

```text
/export -r cleanup data
/export -m normal -t 4 data
```

`requires(otherOption)` checks whether the other option was explicitly specified. `requires(otherOption, value)` checks
the value returned by `ctx.getOption(otherOption)`, so the other option's default value can also satisfy the condition.

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
            })).execute((m, ctx) -> {
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
            argument(new PlayerArgument("player")).execute((player, ctx) -> {
                // Do something
            });
            argument(new Player("player"), new StringArgument("message")).execute((player, message, ctx) -> {
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
