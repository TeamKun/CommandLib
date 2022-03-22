package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.GameProfileArgument;

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
        }).get(), contextAction, GameProfileArgument.gameProfile());
    }

    @Override
    public String parse(CommandContext<CommandSource> ctx) {
        String input = ctx.getInput();

        Optional<ParsedCommandNode<CommandSource>> node = ctx.getNodes().stream()
                .filter(x -> x.getNode().getName().equals(name))
                .findFirst();

        if (node.isPresent()) {
            return node.get().getRange().get(input);
        } else {
            return "";
        }
    }
}
