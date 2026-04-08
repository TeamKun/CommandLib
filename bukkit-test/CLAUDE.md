# bukkit-test module — implementation notes

## Purpose

A test utility module for plugins/libraries that use CommandLib (bukkit).
Provides `CommandTester` and `FakeSender` to execute commands and assert on
results without a running Minecraft server.

## How NMS is bypassed

CommandLib's bukkit module depends heavily on NMS (version-specific Minecraft
internals). Two static mocks are held for the entire lifetime of a
`CommandTester` instance:

| Mock | Purpose |
|---|---|
| `mockStatic(NMSReflection.class)` | `findMinecraftClass()` returns `Object.class`, making the `MinecraftClass` constructor chain work without a real server |
| `mockStatic(NMSClassRegistry.class)` | `findClass(X.class)` returns the corresponding `MockNMSxxx` class instead of the real version-specific implementation |

`CommandTester` implements `AutoCloseable` and tears down both mocks in
`close()`. Always use try-with-resources.

## Package structure

```
net.kunmc.lab.commandlib
├── CommandTester.java       — test executor, holds static mocks
├── FakeSender.java          — Mockito-based sender with message capture
└── nms/
    ├── argument/            — one MockNMSArgumentXxx per NMS argument type
    ├── command/             — MockNMSCommandListenerWrapper
    ├── core/                — other NMS core types (e.g. particle params)
    ├── resources/           — CraftBukkit resource bridges (e.g. CraftParticle)
    └── world/               — NMS world/item/block types and their CraftBukkit wrappers
```

Mock NMS classes live under `nms/` mirroring the production `util/nms/`
structure. New NMS argument types go in `nms/argument/`, other NMS types
follow the same sub-package as production (e.g., `nms/world/`).

## Adding support for a new NMS argument type

1. Create `MockNMSArgumentXxx extends NMSArgumentXxx` in `nms/argument/`.
   - Public no-arg constructor calling `super(null, "Mock")`.
   - `argument()` returns a brigadier-native type (usually `StringArgumentType.word()`).
   - `parseImpl()` resolves the value from `CommandTester.getFakeEntity()` or
     a new static accessor added to `CommandTester`.
2. Register in `CommandTester` constructor:

```java
class CommandTester {
    CommandTester(String permissionPrefix, Supplier<Collection<? extends Command>> commandsSupplier) {
        // ... existing setup ...
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentXxx.class))
                       .thenAnswer(inv -> MockNMSArgumentXxx.class);
    }
}
```

The default stub for unknown `findClass()` calls throws
`UnsupportedOperationException` with a clear message.

## Shared state between CommandTester and mock NMS classes

Mock NMS classes are instantiated via reflection (`ReflectionUtil.getConstructor`)
and therefore cannot receive state through constructors. They access the active
`CommandTester` instance through two public static accessors:

- `CommandTester.getFakeEntity(String name)` — for argument mocks
- `CommandTester.getCurrentCommandSender()` — for MockNMSCommandListenerWrapper

These methods are intended for mock NMS class use only (documented as such).
`CommandTester.current` is set in the constructor and cleared in `close()`.

## Mocking Bukkit static methods in tests

Some arguments call Bukkit static APIs at parse time (`WorldArgument`,
`OfflinePlayerArgument`, `TeamArgument`, etc.). Use `mockito-inline`'s
`MockedStatic` in the same try-with-resources block as `CommandTester`:

```java
class WorldArgumentTest {
    @Test
    void world_is_resolved_by_name() {
        FakeSender sender = FakeSender.player("Alice");
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getName()).thenReturn("nether");
        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class);
             CommandTester tester = new CommandTester(new Command("tp") {{
                 argument(new WorldArgument("world"), (world, ctx) -> ctx.sendMessage(world.getName()));
             }}, "test.command")) {
            bukkit.when(() -> Bukkit.getWorld("nether")).thenReturn(mockWorld);
            tester.execute("tp nether", sender);
        }
        assertThat(sender.getSentMessageTexts()).containsExactly("nether");
    }
}
```

Similarly, arguments backed by non-Bukkit registries (`EnchantmentArgument`
→ `Enchantment.getByKey()`, `PotionEffectArgument` → `PotionEffectType.getByName()`)
need `MockedStatic` for their respective classes.

Resources in a multi-resource try-with-resources close in reverse declaration
order, so `CommandTester` closes before the Bukkit mock — this is the correct
order and requires no special handling.

## MockBukkit compatibility

`execute()` accepts both `FakeSender` (Mockito-based, with message capture) and
plain `CommandSender` (e.g., MockBukkit's `PlayerMock`):

```java
class MyCommandTest {
    @Test
    void test_with_fake_sender() {
        FakeSender sender = FakeSender.player("Steve");
        try (CommandTester tester = new CommandTester(new MyCommand(), "myplugin.command")) {
            tester.execute("cmd arg", sender);
        }
        assertThat(sender.getSentMessageTexts()).contains("expected");
    }

    @Test
    void test_with_mockbukkit_player() {
        PlayerMock player = server.addSimplePlayer();
        try (CommandTester tester = new CommandTester(new MyCommand(), "myplugin.command")) {
            tester.execute("cmd arg", player);
        }
        player.assertSaid("expected");
    }
}
```

`MockNMSCommandListenerWrapper` derives `getBukkitEntity()`, `getBukkitWorld()`,
and `getBukkitLocation()` from the sender when it implements `Entity`. This
means MockBukkit's `PlayerMock` (which has real location/world) is automatically
used; Mockito mocks return `null` by default unless stubbed.

## FakeSender

- `getSentMessages()` returns `List<BaseComponent>` — preserves color/formatting.
- `getSentMessageTexts()` returns `List<String>` — plain text, color stripped.
- `asSender()` exposes the underlying Mockito mock for additional setup.
- All permissions return `true` by default. To test permission-denied behaviour:

```java
class MyCommandTest {
    @Test
    void permission_denied() {
        FakeSender player = FakeSender.player("Steve");
        Mockito.when(player.asSender().hasPermission(Mockito.anyString())).thenReturn(false);
        // ... execute and assert failure
    }
}
```
