package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.suggestion.SuggestionAction;
import net.minecraft.command.arguments.GameProfileArgument;

import java.util.function.Supplier;

public class UnparsedArgument extends Argument<String, UnparsedArgument> {
    public UnparsedArgument(String name) {
        super(name, GameProfileArgument.gameProfile());
        displayDefaultSuggestions(false);
        suggestionAction(((Supplier<SuggestionAction<CommandContext>>) () -> {
            if (suggestionAction() == null) {
                return sb -> {
                };
            }
            return suggestionAction();
        }).get());
    }

    @Override
    public String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    protected String parseImpl(CommandContext ctx) {
        return ctx.getInput(name());
    }
}
