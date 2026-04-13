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
                if (!ctx.getSender().hasPermission("myplugin.game")) {
                    throw new CommandPrerequisiteException("No permission");
                }
            });
            addChildren(new StartCommand(), new StopCommand());
        }});
    }
}
```
