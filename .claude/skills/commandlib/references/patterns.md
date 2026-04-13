# CommandLib Usage Patterns

## Argument Style

Prefer Style A, the typed shorthand, over the builder style for normal command
implementations.

```java
class MyCommand extends Command {
    MyCommand() {
        super("message");

        argument(new PlayerArgument("target"), new StringArgument("text"), (target, text, ctx) -> {
            target.sendMessage(text);
        });
    }
}
```

Use the builder style only when the typed shorthand cannot express the required
shape.

Options such as validation and suggestions are passed to the argument
constructor:

```java
class MyCommand extends Command {
    MyCommand() {
        super("cmd");

        argument(new PlayerArgument("target",
                                    opt -> opt.validator(player -> player.isOp())
                                              .suggestionAction((sb, ctx) -> sb.suggest("Steve"))),
                 (player, ctx) -> ctx.sendMessage("ok"));
    }
}
```

## Command Options

Use `CommandOption<T, CommandContext>` for options such as `-f`, `--force`, and
`--limit 10`. Register options on the command, then read them with
`ctx.getOption(option)`.

```java
class ScanCommand extends Command {
    ScanCommand() {
        super("scan");

        CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f')
                                                                     .description("Force execution"));
        CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100)
                                                                     .description("Maximum count"));
        CommandOption<String, CommandContext> format = option(Options.string("format", 'F', "text")
                                                                     .description("Output format"));

        argument(new StringArgument("target", StringArgument.Type.WORD), (target, ctx) -> {
            boolean isForce = ctx.getOption(force);
            int maxCount = ctx.getOption(limit);
            String outputFormat = ctx.getOption(format);
            boolean limitWasSpecified = ctx.hasOption(limit);
            ctx.sendMessage(target + ":" + isForce + ":" + maxCount + ":" + outputFormat + ":" + limitWasSpecified);
        });
    }
}
```

Supported forms:

```text
/scan alex
/scan -f alex
/scan --force alex
/scan -fv alex
/scan -n 20 alex
/scan --limit 20 alex
/scan -f -n 20 --format json alex
```

Important constraints:

- Options must appear immediately after the command or subcommand name, before
  regular arguments.
- For child commands, options belong to the most specific child command:
  `/game start -f arena`, not `/game -f start arena`.
- Value options use separated values only: `--limit 20` and `-n 20`; do not
  generate `--limit=20` or `-n20`.
- Prefer typed option keys over `ctx.getParsedArg(...)`.
- Use `ctx.hasOption(option)` when code must distinguish explicit presence from
  a default value.
- Add `.description(...)` for user-facing commands so help output is clear.
- Use `.requires(...)` for option dependencies.

Available factories:

```java
Options.flag("force", 'f')
Options.bool("enabled", 'e', true)
Options.integer("limit", 'n', 10)
Options.longValue("size", 's', 0L)
Options.floatValue("speed", 'S', 1.0f)
Options.doubleValue("radius", 'r', 5.0)
Options.string("format", 'F', "text")
```
