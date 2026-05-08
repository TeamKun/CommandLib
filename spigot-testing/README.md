# CommandLib spigot-testing

A testing utility for plugins and libraries that use [CommandLib](../README.md).  
It lets you execute commands and assert on sent messages without a running Minecraft server.

## Installation

Add the dependency to your test scope. Replace `latest.release` with a specific version.

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    testImplementation "com.github.Maru32768.CommandLib:spigot-testing:latest.release"
}
```

## Usage

### 1. Create a `CommandTester`

Pass the command(s) you want to test and the permission prefix used when registering them.
`CommandTester` implements `AutoCloseable` — always use try-with-resources.

```java
class GreetCommandTest {
    @Test
    void greet_test() {
        try (CommandTester tester = new CommandTester(new GreetCommand(), "myplugin.command")) {
            // ...
        }
    }
}
```

### 2. Create a `FakeSender`

```java
class GreetCommandTest {
    @Test
    void greet_test() {
        FakeSender player = FakeSender.player("Steve");   // fake player
        FakeSender console = FakeSender.console();        // fake console
    }
}
```

### 3. Execute a command and assert

```java
class GreetCommandTest {
    @Test
    void greet_sends_message() {
        try (CommandTester tester = new CommandTester(new GreetCommand(), "myplugin.command")) {
            tester.execute("greet Steve", player);
        }

        assertThat(player.getSentMessageTexts()).contains("Hello, Steve!");
    }
}
```

## Full Example

```java
// The command under test
public class HealCommand extends Command {
    public HealCommand() {
        super("heal");
        requirePlayer();
        argument(new PlayerArgument("target")).execute((target, ctx) -> {
            ctx.sendSuccess("Healed " + target.getName() + "!");
        });
    }
}
```

```java
// JUnit 5 test
class HealCommandTest {
    @Test
    void heal_sends_success_message() {
        FakeSender steve = FakeSender.player("Steve");
        FakeSender admin = FakeSender.player("Admin");

        try (CommandTester tester = new CommandTester(new HealCommand(), "myplugin.command")) {
            tester.withFakePlayer((Player) steve.asSender());
            tester.execute("heal Steve", admin);
        }

        assertThat(admin.getSentMessageTexts()).containsExactly("Healed Steve!");
        assertThat(admin.getSentMessages()).extracting(BaseComponent::getColor)
                                           .containsExactly(ChatColor.GREEN);
    }

    @Test
    void heal_is_blocked_for_console() {
        FakeSender console = FakeSender.console();

        try (CommandTester tester = new CommandTester(new HealCommand(), "myplugin.command")) {
            tester.execute("heal Steve", console);
        }

        assertThat(console.getSentMessageTexts()).doesNotContain("Healed Steve!");
    }
}
```

## API Reference

### `CommandTester`

| Constructor                                                                           | Description                                                                                                                                                                                                                        |
|---------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `CommandTester(Command command, String permissionPrefix)`                             | Register a single command                                                                                                                                                                                                          |
| `CommandTester(Supplier<? extends Command> commandSupplier, String permissionPrefix)` | Register a single command using a supplier — **required when the command contains NMS-backed arguments** (e.g. `PlayerArgument`, `EnchantmentArgument`) whose constructors call into `NMSClassRegistry` before the tester is ready |
| `CommandTester(Collection<? extends Command> commands, String permissionPrefix)`      | Register multiple commands                                                                                                                                                                                                         |

Builder API:

```java
class Test {
    void test() {
        try (CommandTester tester = CommandTester.builder()
                                                 .command(() -> new HealCommand())
                                                 .permissionPrefix("myplugin.command")
                                                 .build()) {
            // ...
        }
    }
}
```

Use `mockNmsClass()` when a test needs an NMS mock that is not built into
`spigot-testing` yet:

```java
class Test {
    void test() {
        try (CommandTester tester = CommandTester.builder()
                .mockNmsClass(NMSArgumentXxx.class, MockNMSArgumentXxx.class)
                .command(() -> new XxxCommand())
                .permissionPrefix("myplugin.command")
                .build()) {
            // ...
        }
    }
}
```

| Method                                                     | Description                                                                                                          |
|------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| `void execute(String input, FakeSender sender)`            | Execute a command as the given sender. Throws `RuntimeException` if the input does not match any registered command. |
| `CommandTester withFakePlayer(Player player)`              | Register a fake player by name for use with `PlayerArgument`, `PlayersArgument`, and `EntityArgument`.               |
| `CommandTester withFakeEntity(String name, Entity entity)` | Register a fake entity by name for use with `EntityArgument`.                                                        |
| `void close()`                                             | Tear down all internal mocks. Called automatically by try-with-resources.                                            |

### `FakeSender`

| Factory method                   | Description                                                        |
|----------------------------------|--------------------------------------------------------------------|
| `FakeSender.player(String name)` | A fake player with the given name. Has all permissions by default. |
| `FakeSender.console()`           | A fake console sender. Has all permissions by default.             |

| Method                                     | Description                                                                                                           |
|--------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `List<BaseComponent> getSentMessages()`    | Returns messages sent to this sender. Preserves color and formatting for assertions on `sendSuccess` / `sendFailure`. |
| `List<String> getSentMessageTexts()`       | Convenience method. Returns sent messages as plain text with color codes stripped.                                    |
| `List<String> getSentMessageLegacyTexts()` | Convenience method. Returns sent messages as legacy text with color codes preserved.                                  |
| `CommandSender asSender()`                 | Returns the underlying Mockito mock for additional setup (e.g., stubbing `getLocation()`).                            |

## Notes

- `getSentMessages()` returns `BaseComponent` objects so you can assert on color as well as text:
  ```java
  class MyCommandTest {
      @Test
      void check_color_and_text() {
          // check text
          assertThat(sender.getSentMessageTexts()).containsExactly("done");
          // check color (green = sendSuccess, red = sendFailure, yellow = sendWarn)
          assertThat(sender.getSentMessages())
              .extracting(BaseComponent::getColor)
              .containsExactly(ChatColor.GREEN);
      }
  }
  ```
- For simple text-only assertions, use `getSentMessageTexts()` which strips color codes.
- For snapshot-style assertions where color matters, use `getSentMessageLegacyTexts()`.
- Independent `CommandTester` instances can run concurrently on different test threads. Each tester's active state is
  thread-local, so fake entities, worlds, and the current sender are not shared across parallel test threads.
- All permissions are granted by default. To test permission-denied behaviour, stub `asSender().hasPermission(...)`:
  ```java
  class MyCommandTest {
      @Test
      void deny_all_permissions() {
          FakeSender player = FakeSender.player("Steve");
          Mockito.when(player.asSender().hasPermission(Mockito.anyString())).thenReturn(false);
      }
  }
  ```
- `requirePlayer()` and `requireConsole()` work correctly — `FakeSender.player()` passes `instanceof Player` checks.
- **NMS-backed arguments** (`PlayerArgument`, `EnchantmentArgument`, `ItemStackArgument`, etc.) call into
  `NMSClassRegistry` at construction time. Always use the `Supplier<Command>` constructor form for these:
  ```java
  class EnchantCommandTest {
      @Test
      void enchant_test() {
          // Correct — NMS mocks are active when the supplier is called inside the constructor
          try (CommandTester tester = new CommandTester(
                  () -> new Command("enchant") {{
                      argument(new EnchantmentArgument("type")).execute((ench, ctx) -> { /* ... */ });
                  }},
                  "test.command")) {
              // ...
          }
      }
  }
  ```
- **Arguments that call Bukkit static methods** (`WorldArgument`, `OfflinePlayerArgument`, `TeamArgument`, etc.) require
  `MockedStatic<Bukkit>` from `mockito-inline`. Open it in the same try-with-resources block as `CommandTester`:
  ```java
  class TpCommandTest {
      @Test
      void tp_to_world() {
          FakeSender sender = FakeSender.player("Steve");
          World mockWorld = Mockito.mock(World.class);

          try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class);
               CommandTester tester = new CommandTester(
                       new Command("tp") {{
                           argument(new WorldArgument("world")).execute((world, ctx) -> { /* ... */ });
                       }},
                       "test.command")) {
              bukkit.when(() -> Bukkit.getWorld("nether")).thenReturn(mockWorld);
              tester.execute("tp nether", sender);
          }
      }
  }
  ```
