package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentProfile;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class UnparsedArgument extends Argument<String> {
    public UnparsedArgument(String name) {
        this(name, option -> {
        });
    }

    public UnparsedArgument(String name, Consumer<Option<String>> options) {
        super(name, ArgumentProfile.a());
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
    public String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    public String parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        return getInputString(ctx, name);
    }
}
