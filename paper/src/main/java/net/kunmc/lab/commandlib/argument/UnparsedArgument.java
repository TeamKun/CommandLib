package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;

/**
 * An argument that captures a single whitespace-delimited token as a raw string,
 * without any entity-selector or other special-character validation.
 *
 * <p>This is useful when the value may contain characters that Brigadier's built-in
 * {@code StringArgumentType.word()} rejects, such as {@code @} (e.g. {@code @p},
 * {@code @e[type=zombie]}) or path separators (e.g. {@code @../config/value}).
 */
public class UnparsedArgument extends Argument<String, UnparsedArgument> {
    public UnparsedArgument(String name) {
        super(name, RawWordArgumentType.rawWord());
        suggestionAction(sb -> {
            sb.suggest("test")
              .suggest("sb@aaaa")
              .suggest("@a");
        });
    }

    @Override
    public String cast(Object parsedArgument) {
        return (String) parsedArgument;
    }

    @Override
    protected String parseImpl(CommandContext ctx) throws ArgumentParseException {
        return ctx.getHandle()
                  .getArgument(name(), String.class);
    }
}
