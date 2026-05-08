# CommandLib Paper Testing

`paper-testing` provides lightweight test utilities for CommandLib's Paper API.
It is intended for command-level tests that do not need a running Paper server.

## Example

```java
class Test {
    void test() {
        Command command = new Command("hello");
        command.execute(ctx -> ctx.sendSuccess("Hello"));

        CommandTester tester = new CommandTester(command, "example.command");
        FakeSender sender = FakeSender.player("Steve");

        tester.execute("hello", sender);

        assertThat(sender.getSentMessageTexts()).containsExactly("Hello");
    }
}
```

## Scope

- Executes Paper commands through Brigadier using CommandLib's Paper platform adapter.
- Supports independent `CommandTester` instances running concurrently on different test threads. Each tester's active
  state is thread-local, so fake players, entities, worlds, and registry values are not shared across parallel test
  threads.
- Captures Adventure `Component` messages.
- Exposes Brigadier suggestions so tooltip behavior can be asserted.
- Does not emulate Paper lifecycle registration or a real server command map.
  Registration is covered by the repository's `integration-test` Paper targets.
- Stubs Paper's vanilla argument provider, selectors, registry-backed arguments,
  and resolver-backed argument parsing for command-level tests. Real registration
  and server behavior remain covered by integration tests.
- `EnchantmentArgument` and `PotionEffectArgument` can be wired into commands,
  but command-level execution with concrete registry values requires Paper's
  server-side `RegistryAccess` bootstrap. Verify their real registry values in
  integration tests.
