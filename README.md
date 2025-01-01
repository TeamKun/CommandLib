# CommandLib - A Feature-Rich Minecraft Command API for Bukkit

[![](https://jitpack.io/v/TeamKun/CommandLib.svg)](https://jitpack.io/#TeamKun/CommandLib)

CommandLib is an advanced, type-safe Command API for Bukkit developers. It abstracts and enhances the Minecraft 1.13
command APIs, simplifying command implementation. With features like customizable argument suggestions, seamless
validation, improved usability, and extensibility, CommandLib empowers developers to build better commands with ease.

#### Supported Versions

| Platform   | Tested Versions                                            | Notes                                              |
|------------|------------------------------------------------------------|----------------------------------------------------|
| **Bukkit** | `1.16.5`, `1.19.4`, `1.20.1`, `1.20.4`, `1.20.6`, `1.21.0` | Expected to work on intermediate versions.         |
| **Forge**  | `1.16.5`                                                   | Currently supports only `1.16.5` and works fully.  |
| **Mohist** | `1.16.5`, `1.20.1`                                         | Works on Mohist since it's compatible with Spigot. |

## Features

1. **Type safe arguments**  
   Use arguments in a type-safe manner directly within your code, reducing potential runtime errors.
2. **No need for Brigadier and NMS dependency**  
   Simplify your setup by avoiding Brigadier and NMS in your project.
3. **Powerful suggestion generation**  
   Automatically generate argument suggestions with customizable options for enhanced flexibility.
4. **Support for the `/execute` command**   
   Allow your commands to be executed seamlessly from the `/execute` command, just like built-in commands.  
   ![execute_as](./images/fireworks_execute_as.gif)

## Installation

To ensure stability, we recommend replacing `latest.release` with a specific version such as `0.16.0`.  
You can find the latest version on the [releases page](https://github.com/TeamKun/CommandLib/releases).

<details>
<summary>Bukkit</summary>

```groovy
plugins {
    id "com.gradleup.shadow" version "8.3.5"
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation "com.github.TeamKun.CommandLib:bukkit:latest.release"
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
<summary>Forge</summary>

```groovy
plugins {
    id "com.gradleup.shadow" version "8.3.5"
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation "com.github.TeamKun.CommandLib:forge:latest.release"
}

shadowJar {
    archiveFileName = "${rootProject.name}-${project.version}.jar"
    dependencies {
        include(dependency("com.github.TeamKun.CommandLib:forge:.*"))
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

## Code Examples

<details>
<summary>Simple command</summary>

```java
public final class TestPlugin extends JavaPlugin {
    public void onEnable() {
        CommandLib.register(this, new Command("message") {{
            argument(new PlayerArgument("target"), new StringArgument("message"), (target, message, ctx) -> {
                target.sendMessage(message);
            });
        }});
    }
}
```

</details>

<details>
<summary>Subcommands</summary>

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
<summary>Suggesting materials that is block</summary>

```java
public final class TestPlugin extends JavaPlugin {
    public void onEnable() {
        CommandLib.register(this, new Command("test") {{
            argument(new EnumArgument<>("block", Material.class, option -> {
                option.filter(x -> {
                    if (!x.isBlock()) {
                        // Shows the sender this error message
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
<summary>Variable length arguments</summary>

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

## Sample Projects

[Bukkit](./sample/bukkit)  
[Forge](./sample/forge)
