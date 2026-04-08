# CommandLib Conventions and Recommended Patterns

## Argument style

**Prefer Style A (shorthand) over Style B (builder).**

```java
class MyCommand extends Command {
    MyCommand() {
        super("message");
        // Style A — recommended. Type-safe, no cast, no string-based name lookup.
        argument(new PlayerArgument("target"), new StringArgument("text"), (target, text, ctx) -> {
            target.sendMessage(text);
        });
        // Style B — not recommended for normal use.
        // Requires manual cast and string-based name lookup, risks ClassCastException and typos.
        // Use only when Style A cannot cover the use case.
        argument(builder -> {
            builder.playerArgument("target")
                   .stringArgument("text")
                   .execute(ctx -> {
                       Player p = ctx.getParsedArg("target", Player.class);
                       String msg = ctx.getParsedArg("text", String.class);
                       p.sendMessage(msg);
                   });
        });
    }
}
```

Options (validation, custom suggestions, etc.) are passed to the argument constructor in Style A:

```java
class MyCommand extends Command {
    MyCommand() {
        super("cmd");
        argument(new PlayerArgument("target",
                                    opt -> opt.validator(player -> player.isOp())
                                              .suggestionAction((sb, ctx) -> sb.suggest("Steve"))), (player, ctx) -> {
            ctx.sendMessage("ok");
        });
    }
}
```

## Registration

Always specify a permission prefix that matches the plugin's namespace:

```java
class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Good
        CommandLib.register(this, "myplugin.command", new MyCommand());
        // Avoid — uses "minecraft.command" namespace which is semantically wrong for custom plugins
        CommandLib.register(this, new MyCommand());
    }
}
```

## Permissions

```java
class MyCommand extends Command {
    MyCommand() {
        super("spawn");
        permission("myplugin.command.spawn");                          // custom node — ignores prefix
        permission("myplugin.command.spawn", PermissionDefault.FALSE); // with explicit default
        permission(PermissionDefault.OP);                              // auto-generated node, OP only
    }
}
```

## Subcommands and prerequisites

Prerequisites are inherited by children by default. Define shared checks on the parent:

```java
class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandLib.register(this, "myplugin.command", new Command("game") {{
            addPrerequisite(ctx -> {
                if (!ctx.getSender()
                        .hasPermission("myplugin.game")) {
                    throw new CommandPrerequisiteException("権限がありません");
                }
            });
            addChildren(new StartCommand(), new StopCommand()); // both inherit the check
        }});
    }
}
```

## Testing with CommandTester

Use `bukkit-test`'s `CommandTester` and `FakeSender` to test commands without a running server.

```java
class MyCommandTest {
    @Test
    void basic_test() {
        FakeSender sender = FakeSender.player("Alice");
        try (CommandTester tester = new CommandTester(new MyCommand(), "myplugin.command")) {
            tester.execute("mycmd arg", sender);
        }
        assertThat(sender.getSentMessageTexts()).containsExactly("expected message");
    }
}
```

**NMS-backed arguments** (`PlayerArgument`, `EnchantmentArgument`, `ItemStackArgument`, etc.)
call `NMSClassRegistry` in their constructors. Use the `Supplier<Command>` constructor form so
that the NMS mocks are active when the command is built:

```java
class HealCommandTest {
    @Test
    void heal_test() {
        FakeSender sender = FakeSender.player("Steve");
        try (CommandTester tester = new CommandTester(() -> new Command("heal") {{
            argument(new PlayerArgument("target"), (target, ctx) -> ctx.sendMessage("healed " + target.getName()));
        }}, "myplugin.command")) {
            tester.withFakePlayer((Player) sender.asSender());
            tester.execute("heal Steve", sender);
        }
        assertThat(sender.getSentMessageTexts()).containsExactly("healed Steve");
    }
}
```

**Arguments that call Bukkit static APIs** (`WorldArgument`, `OfflinePlayerArgument`,
`TeamArgument`, etc.) need `MockedStatic<Bukkit>` alongside `CommandTester`:

```java
class TpCommandTest {
    @Test
    void tp_test() {
        FakeSender sender = FakeSender.player("Alice");
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getName())
               .thenReturn("nether");
        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class); CommandTester tester = new CommandTester(
                new Command("tp") {{
                    argument(new WorldArgument("world"), (world, ctx) -> ctx.sendMessage(world.getName()));
                }},
                "myplugin.command")) {
            bukkit.when(() -> Bukkit.getWorld("nether"))
                  .thenReturn(mockWorld);
            tester.execute("tp nether", sender);
        }
        assertThat(sender.getSentMessageTexts()).containsExactly("nether");
    }
}
```
