# CommandLib Usage Patterns

## Argument Style

Prefer typed argument instances with `argument(...).execute(...)` over the
builder style for normal command implementations.

```java
class MyCommand extends Command {
    MyCommand() {
        super("message");

        argument(new PlayerArgument("target"), new StringArgument("text")).execute((target, text, ctx) -> {
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

        argument(new PlayerArgument("target", opt -> {
            opt.validator(player -> player.isOp())
               .suggestionAction((sb, ctx) -> sb.suggest("Steve"));
        })).execute((player, ctx) -> {
            ctx.sendMessage("ok");
        });
    }
}
```

## Subcommand Shape

Prefer literal-first subcommands for ordinary plugin commands:

```text
/config get <key>
/config set <key> <value>
/arena start <name>
/arena delete <name>
```

Use argument-first subcommands only when the requested syntax needs it, such as
matching a vanilla-like command tree or an existing command contract:

```text
/config <key> get
/config <key> set <value>
```

Use typed argument instances for this shape. The `child(...)` factory receives
the `Argument` instances, not parsed values, because child commands are built
before a command is executed. Read parent values inside the child executor with
`ctx.getParsedArg(argument)`.

The same argument branch can define multiple children by calling `child(...)`
multiple times:

```java
class ConfigCommand extends Command {
    ConfigCommand() {
        super("config");

        argument(new StringArgument("key")).description("Select a config key")
                                           .child(keyArg -> new Command("get") {{
                                               execute(ctx -> {
                                                   String key = ctx.getParsedArg(keyArg);
                                                   ctx.sendMessage("get " + key);
                                               });
                                           }})
                                           .child(keyArg -> new Command("set") {{
                                               argument(new StringArgument("value")).execute((value, ctx) -> {
                                                   String key = ctx.getParsedArg(keyArg);
                                                   ctx.sendMessage("set " + key + " to " + value);
                                               });
                                           }});
    }
}
```

Do not write the factory as if it receives runtime values:

```java
class MyCommand extends Command {
    MyCommand() {
        // Wrong: keyValue is an Argument object, not the parsed String value.
        argument(new StringArgument("key")).child(keyValue -> new Command("get") {{
            execute(ctx -> ctx.sendMessage(keyValue.toString()));
        }});
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

        argument(new StringArgument("text", StringArgument.Type.WORD)).execute((text, ctx) -> {
            boolean isForce = ctx.getOption(force);
            int maxCount = ctx.getOption(limit);
            String outputFormat = ctx.getOption(format);
            boolean limitWasSpecified = ctx.hasOption(limit);
            ctx.sendMessage(text + ":" + isForce + ":" + maxCount + ":" + outputFormat + ":" + limitWasSpecified);
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
