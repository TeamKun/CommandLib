package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;

/**
 * An argument that captures a single whitespace-delimited token as a raw string,
 * without any entity-selector or other special-character validation.
 *
 * <p>This is useful when the value may contain characters that Brigadier's built-in
 * {@code StringArgumentType.word()} rejects, such as {@code @} (e.g. {@code @p}).
 *
 * <p><b>Note:</b> In Paper's native command system this argument is advertised to
 * clients as {@code brigadier:string GREEDY_PHRASE}, so it should be placed last
 * in any command that uses it.
 */
public class UnparsedArgument extends Argument<String, UnparsedArgument> {
    public UnparsedArgument(String name) {
        super(name, StringArgumentType.greedyString());
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
