package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;

public class FloatArgument extends Argument<Float> {
    public FloatArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction, Float min, Float max) {
        super(name, suggestionAction, contextAction, FloatArgumentType.floatArg(min, max));
    }

    @Override
    public Float parse(CommandContext<CommandSource> ctx) {
        return FloatArgumentType.getFloat(ctx, name);
    }
}
