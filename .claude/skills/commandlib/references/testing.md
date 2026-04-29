# Testing CommandLib Usage

Use `spigot-testing`'s `CommandTester` and `FakeSender` to test commands without a
running Minecraft server. `spigot-testing` works for commands written against both
the `spigot` and `paper` artifacts — no separate `paper-test` artifact exists yet.

Add the dependency in test scope:

```kotlin
dependencies {
    testImplementation("com.github.Maru32768.CommandLib:spigot-testing:latest.release")
}
```

```java
class MyCommandTest {
    @Test
    void basicTest() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new MyCommand(), "myplugin.command")) {
            tester.execute("mycmd arg", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("expected message");
    }
}
```

## NMS-backed Arguments

NMS-backed arguments such as `PlayerArgument`, `EnchantmentArgument`, and
`ItemStackArgument` call `NMSClassRegistry` in their constructors. Use the
`Supplier<Command>` constructor form so the NMS mocks are active when the
command is built.

```java
class HealCommandTest {
    @Test
    void healTest() {
        FakeSender sender = FakeSender.player("Steve");

        try (CommandTester tester = new CommandTester(() -> new Command("heal") {{
            argument(new PlayerArgument("target")).execute((target, ctx) -> ctx.sendMessage("healed " + target.getName()));
        }}, "myplugin.command")) {
            tester.withFakePlayer((Player) sender.asSender());
            tester.execute("heal Steve", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("healed Steve");
    }
}
```

## Bukkit Static APIs

Arguments that call Bukkit static APIs at parse time, such as `WorldArgument`,
`OfflinePlayerArgument`, and `TeamArgument`, need `mockito-inline`
`MockedStatic` in the same try-with-resources block as `CommandTester`.

```java
class TpCommandTest {
    @Test
    void tpTest() {
        FakeSender sender = FakeSender.player("Alice");
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getName())
               .thenReturn("nether");

        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class); CommandTester tester = new CommandTester(
                new Command("tp") {{
                    argument(new WorldArgument("world")).execute((world, ctx) -> ctx.sendMessage(world.getName()));
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

Arguments backed by non-Bukkit static registries, such as `EnchantmentArgument`
or `PotionEffectArgument`, need `MockedStatic` for their respective classes.
