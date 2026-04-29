# spigot-testing Module Notes

## Purpose

`spigot-testing` is a test utility module for plugins and libraries that use
CommandLib's Spigot/Bukkit API. It provides `CommandTester` and `FakeSender` so command
execution can be tested without a running Minecraft server.

## How NMS Is Bypassed

CommandLib's `spigot` module depends on NMS, the version-specific Minecraft
internals. `CommandTester` keeps two static mocks alive for the lifetime of a
tester instance:

| Mock                                 | Purpose                                                                                                                  |
|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| `mockStatic(NMSReflection.class)`    | `findMinecraftClass()` returns `Object.class`, making the `MinecraftClass` constructor chain work without a real server. |
| `mockStatic(NMSClassRegistry.class)` | `findClass(X.class)` returns the corresponding `MockNMS...` class instead of the real version-specific implementation.   |

`CommandTester` implements `AutoCloseable` and tears down both mocks in
`close()`. Tests should use try-with-resources.

## Package Structure

```text
net.kunmc.lab.commandlib
|-- CommandTester.java
|-- FakeSender.java
`-- nms/
    |-- argument/
    |-- command/
    |-- core/
    |-- resources/
    `-- world/
```

Mock NMS classes live under `nms/`, mirroring the production `util/nms`
structure. New NMS argument types go in `nms/argument/`; other NMS types should
follow the same sub-package as production.

## Adding A New NMS Argument Mock

1. Create `MockNMSArgumentXxx extends NMSArgumentXxx` in `nms/argument/`.
2. Add a public no-arg constructor that calls the production superclass with
   mock-safe values.
3. Make `argument()` return a Brigadier-native type.
4. Implement `parseImpl()` using `CommandTester` static accessors when runtime
   state is needed.
5. Register the mock in the `CommandTester` constructor with
   `NMSClassRegistry.findClass(NMSArgumentXxx.class)`.

The default stub for unknown `findClass()` calls should throw an
`UnsupportedOperationException` with a clear message.

## Shared State

Mock NMS classes are instantiated through reflection, so they cannot receive
state through constructors. They access the active tester through public static
accessors such as:

- `CommandTester.getFakeEntity(String name)`
- `CommandTester.getCurrentCommandSender()`

These methods are intended for mock NMS classes only. `CommandTester.current`
is set in the constructor and cleared in `close()`.

## Bukkit Static APIs

Arguments that call Bukkit static APIs at parse time, such as `WorldArgument`,
`OfflinePlayerArgument`, and `TeamArgument`, need `mockito-inline`
`MockedStatic` in the same try-with-resources block as `CommandTester`.

Resources in a multi-resource try-with-resources block close in reverse
declaration order. Declare static Bukkit mocks before `CommandTester` so
`CommandTester` closes first.

## MockBukkit Compatibility

`execute()` accepts both `FakeSender` and plain `CommandSender` instances such
as MockBukkit's `PlayerMock`. `MockNMSCommandListenerWrapper` derives Bukkit
entity, world, and location from the sender when it implements `Entity`.

`FakeSender` preserves sent `BaseComponent` values and exposes plain text with
color stripped through `getSentMessageTexts()`.
