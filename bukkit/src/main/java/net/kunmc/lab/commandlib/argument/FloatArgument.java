package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

public class FloatArgument extends Argument<Float> {
    public FloatArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction, Float min, Float max) {
        super(name, suggestionAction, contextAction, FloatArgumentType.floatArg(min, max));
    }

    @Override
    public Float parse(CommandContext<CommandListenerWrapper> ctx) {
        return FloatArgumentType.getFloat(ctx, name);
    }
}
