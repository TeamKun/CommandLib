# CommandLib bukkit-test

A testing utility for plugins and libraries that use [CommandLib](../README.md).  
It lets you execute commands and assert on sent messages without a running Minecraft server.

## Installation

Add the dependency to your test scope. Replace `latest.release` with a specific version.

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    testImplementation "com.github.Maru32768.CommandLib:bukkit-test:latest.release"
}
```

## Usage

### 1. Create a `CommandTester`

Pass the command(s) you want to test and the permission prefix used when registering them.
`CommandTester` implements `AutoCloseable` — always use try-with-resources.

```java
try (CommandTester tester = new CommandTester(new GreetCommand(), "myplugin.command")) {
    // ...
}
```

### 2. Create a `FakeSender`

```java
FakeSender player = FakeSender.player("Steve");   // fake player
FakeSender console = FakeSender.console();          // fake console
```

### 3. Execute a command and assert

```java
tester.execute("greet Steve", player);

assertThat(player.getSentMessageTexts()).contains("Hello, Steve!");
```

## Full Example

```java
// The command under test
public class HealCommand extends Command {
    public HealCommand() {
        super("heal");
        requirePlayer();
        argument(new PlayerArgument("target"), (target, ctx) -> {
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
        assertThat(admin.getSentMessages())
                .extracting(BaseComponent::getColor)
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

| Method                                  | Description                                                                                                           |
|-----------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `List<BaseComponent> getSentMessages()` | Returns messages sent to this sender. Preserves color and formatting for assertions on `sendSuccess` / `sendFailure`. |
| `List<String> getSentMessageTexts()`    | Convenience method. Returns sent messages as plain text with color codes stripped.                                    |
| `CommandSender asSender()`              | Returns the underlying Mockito mock for additional setup (e.g., stubbing `getLocation()`).                            |

## Notes

- `getSentMessages()` returns `BaseComponent` objects so you can assert on color as well as text:
  ```java
  // check text
  assertThat(sender.getSentMessageTexts()).containsExactly("done");
  // check color (green = sendSuccess, red = sendFailure, yellow = sendWarn)
  assertThat(sender.getSentMessages())
      .extracting(BaseComponent::getColor)
      .containsExactly(ChatColor.GREEN);
  ```
- For simple text-only assertions, use `getSentMessageTexts()` which strips color codes.
- All permissions are granted by default. To test permission-denied behaviour, stub `asSender().hasPermission(...)`:
  ```java
  FakeSender player = FakeSender.player("Steve");
  Mockito.when(player.asSender().hasPermission(Mockito.anyString())).thenReturn(false);
  ```
- `requirePlayer()` and `requireConsole()` work correctly — `FakeSender.player()` passes `instanceof Player` checks.
- **NMS-backed arguments** (`PlayerArgument`, `EnchantmentArgument`, `ItemStackArgument`, etc.) call into
  `NMSClassRegistry` at construction time. Always use the `Supplier<Command>` constructor form for these:
  ```java
  // Correct — NMS mocks are active when the supplier is called inside the constructor
  new CommandTester(() -> new Command("enchant") {{
      argument(new EnchantmentArgument("type"), (ench, ctx) -> { ... });
  }}, "test.command")
  ```
- **Arguments that call Bukkit static methods** (`WorldArgument`, `OfflinePlayerArgument`, `TeamArgument`, etc.) require
  `MockedStatic<Bukkit>` from `mockito-inline`. Open it in the same try-with-resources block as `CommandTester`:
  ```java
  try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class);
       CommandTester tester = new CommandTester(new Command("tp") {{
           argument(new WorldArgument("world"), (world, ctx) -> { ... });
       }}, "test.command")) {
      bukkit.when(() -> Bukkit.getWorld("nether")).thenReturn(mockWorld);
      tester.execute("tp nether", sender);
  }
  ```
