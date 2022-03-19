package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;

public class IntegerArgument extends Argument<Integer> {
    public IntegerArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction, Integer min, Integer max) {
        super(name, suggestionAction, contextAction, IntegerArgumentType.integer(min, max));
    }

    @Override
    public Integer parse(CommandContext<CommandSource> ctx) {
        return IntegerArgumentType.getInteger(ctx, name);
    }
}
