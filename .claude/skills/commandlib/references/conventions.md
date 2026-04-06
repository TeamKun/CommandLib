# CommandLib Conventions and Recommended Patterns

## Argument style

**Prefer Style A (shorthand) over Style B (builder).**

```java
// Style A — recommended. Type-safe, no cast, no string-based name lookup.
argument(new PlayerArgument("target"), new StringArgument("message"), (player, message, ctx) -> {
    player.sendMessage(message);
});

// Style B — not recommended for normal use.
// Requires manual cast and string-based name lookup, risks ClassCastException and typos.
// Use only when Style A cannot cover the use case.
argument(builder -> {
    builder.playerArgument("target")
           .stringArgument("message")
           .execute(ctx -> {
               Player p = ctx.getParsedArg("target", Player.class);
               String msg = ctx.getParsedArg("message", String.class);
               p.sendMessage(msg);
           });
});
```

Options (validation, custom suggestions, etc.) are passed to the argument constructor in Style A:

```java
argument(new PlayerArgument("target", opt -> opt
    .validator(player -> player.isOp())
    .suggestionAction((sb, ctx) -> { ... })
), (player, ctx) -> { ... });
```

## Registration

Always specify a permission prefix that matches the plugin's namespace:

```java
// Good
CommandLib.register(this, "myplugin.command", new MyCommand());

// Avoid — uses "minecraft.command" namespace which is semantically wrong for custom plugins
CommandLib.register(this, new MyCommand());
```

## Permissions

```java
permission("myplugin.command.spawn");                          // custom node — ignores prefix
permission("myplugin.command.spawn", PermissionDefault.FALSE); // with explicit default
permission(PermissionDefault.OP);                              // auto-generated node, OP only
```

## Subcommands and prerequisites

Prerequisites are inherited by children by default. Define shared checks on the parent:

```java
new Command("game") {{
    addPrerequisite(ctx -> {
        if (!ctx.getSender().hasPermission("myplugin.game")) {
            throw new CommandPrerequisiteException("権限がありません");
        }
    });
    addChildren(new StartCommand(), new StopCommand()); // both inherit the check
}}
```
