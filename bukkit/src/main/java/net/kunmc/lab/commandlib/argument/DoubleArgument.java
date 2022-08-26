package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.function.Consumer;

public class DoubleArgument extends Argument<Double> {
    public DoubleArgument(String name, Consumer<Option<Double>> options) {
        super(name, DoubleArgumentType.doubleArg(-Double.MAX_VALUE, Double.MAX_VALUE));
        setOptions(options);
    }

    public DoubleArgument(String name, Consumer<Option<Double>> options, Double min, Double max) {
        super(name, DoubleArgumentType.doubleArg(min, max));
        setOptions(options);
    }

    @Override
    public Double parse(CommandContext<CommandListenerWrapper> ctx) {
        return DoubleArgumentType.getDouble(ctx, name);
    }
}
