package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.arguments.GameProfileArgument;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class UnparsedArgument extends Argument<String> {
    public UnparsedArgument(String name) {
        this(name, option -> {
        });
    }

    public UnparsedArgument(String name, Consumer<Option<String, CommandContext>> options) {
        super(name, GameProfileArgument.gameProfile());
        setDisplayDefaultSuggestions(false);
        setOptions(options);
        setSuggestionAction(((Supplier<SuggestionAction<CommandContext>>) () -> {
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
