package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentProfile;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class UnparsedArgument extends Argument<String> {
    private final Predicate<? super String> filter;

    public UnparsedArgument(String name, SuggestionAction suggestionAction, Predicate<? super String> filter, ContextAction contextAction) {
        super(name, ((Supplier<SuggestionAction>) () -> {
            if (suggestionAction == null) {
                return sb -> {
                };
            }

            return suggestionAction;
        }).get(), contextAction, ArgumentProfile.a());
        this.filter = filter;
    }

    @Override
    public String parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String s = getInputString(ctx, name);
        if (filter == null || filter.test(s)) {
            return s;
        }
        throw new IncorrectArgumentInputException(this, ctx, s);
    }
}
