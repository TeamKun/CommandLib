package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.ArgumentProfile;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.Optional;
import java.util.function.Supplier;

public class UnparsedArgument extends Argument<String> {
    public UnparsedArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, ((Supplier<SuggestionAction>) () -> {
            if (suggestionAction == null) {
                return sb -> {
                };
            }

            return suggestionAction;
        }).get(), contextAction, ArgumentProfile.a());
    }

    @Override
    public String parse(CommandContext<CommandListenerWrapper> ctx) {
        String input = ctx.getInput();

        Optional<ParsedCommandNode<CommandListenerWrapper>> node = ctx.getNodes().stream()
                .filter(x -> x.getNode().getName().equals(name))
                .findFirst();

        return node.map(x -> x.getRange().get(input)).orElse("");
    }
}
