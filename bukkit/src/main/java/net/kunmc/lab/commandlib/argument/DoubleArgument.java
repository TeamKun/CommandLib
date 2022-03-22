package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;

public class DoubleArgument extends Argument<Double> {
    public DoubleArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction, Double min, Double max) {
        super(name, suggestionAction, contextAction, DoubleArgumentType.doubleArg(min, max));
    }

    @Override
    public Double parse(CommandContext<CommandSource> ctx) {
        return DoubleArgumentType.getDouble(ctx, name);
    }
}
