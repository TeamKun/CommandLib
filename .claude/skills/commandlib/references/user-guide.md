# CommandLib User Guide

This reference is for downstream Bukkit plugin or library projects that depend
on CommandLib. It is not repository-maintenance guidance for CommandLib itself.

## Registration

Always specify a permission prefix that matches the plugin namespace:

```java
class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandLib.register(this, "myplugin.command", new MyCommand());
    }
}
```

Avoid relying on the default registration overload for custom plugins because
it uses a generic namespace that is usually not the plugin's permission model.

## Permissions

```java
class MyCommand extends Command {
    MyCommand() {
        super("spawn");

        permission("myplugin.command.spawn");
        permission("myplugin.command.spawn", PermissionDefault.FALSE);
        permission(PermissionDefault.OP);
    }
}
```

## Subcommands And Prerequisites

Prerequisites are inherited by children by default. Define shared checks on the
parent command:

```java
class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandLib.register(this, "myplugin.command", new Command("game") {{
            addPrerequisite(ctx -> {
                if (!ctx.getSender()
                        .hasPermission("myplugin.game")) {
                    throw new CommandPrerequisiteException("No permission");
                }
            });
            addChildren(new StartCommand(), new StopCommand());
        }});
    }
}
```

Argument chains can also have child commands. Prefer this only for vanilla-like
or compatibility syntax where the target comes before the action.

The `child(...)` factory receives `Argument` instances, not parsed runtime
values. Use those instances with `ctx.getParsedArg(argument)` inside the child
executor:

```java
class ConfigCommand extends Command {
    ConfigCommand() {
        super("config");

        argument(new StringARgument("key")).description("Select a config key")
                                           .child(keyArg -> new Command("get") {{
                                               execute(ctx -> {
                                                   String parsedKey = ctx.getParsedArg(keyArg);
                                                   ctx.sendMessage("get " + parsedKey);
                                               });
                                           }})
                                           .child(keyArg -> new Command("set") {{
                                               argument(new StringArgument("value")).execute((parsedValue, ctx) -> {
                                                   String parsedKey = ctx.getParsedArg(keyArg);
                                                   ctx.sendMessage("set " + parsedKey + " to " + parsedValue);
                                               });
                                           }});
    }
}
```

Calling `child(...)` repeatedly appends multiple child commands under the same
argument branch.
