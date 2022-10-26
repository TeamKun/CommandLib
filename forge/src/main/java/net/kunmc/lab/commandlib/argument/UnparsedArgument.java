package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.GameProfileArgument;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class UnparsedArgument extends Argument<String> {
    public UnparsedArgument(String name) {
        this(name, option -> {
        });
    }

    public UnparsedArgument(String name, Consumer<Option<String>> options) {
        super(name, GameProfileArgument.gameProfile());
        setOptions(options);
        setSuggestionAction(((Supplier<SuggestionAction>) () -> {
            if (suggestionAction() == null) {
                return sb -> {
                };
            }
            return suggestionAction();
        }).get());
    }

    @Override
    protected String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    public String parse(CommandContext<CommandSource> ctx) {
        return getInputString(ctx, name);
    }
}
