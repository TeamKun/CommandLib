package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class DoubleArgument extends Argument<Double> {
    public DoubleArgument(String name) {
        this(name, option -> {
        });
    }

    public DoubleArgument(String name, Consumer<Option<Double>> options) {
        super(name, DoubleArgumentType.doubleArg(-Double.MAX_VALUE, Double.MAX_VALUE));
        setOptions(options);
    }

    public DoubleArgument(String name, Double min, Double max) {
        this(name, option -> {
        }, min, max);
    }

    public DoubleArgument(String name, Consumer<Option<Double>> options, Double min, Double max) {
        super(name, DoubleArgumentType.doubleArg(min, max));
        setOptions(options);
    }

    @Override
    public Double cast(Object parsedArgument) {
        return ((Double) parsedArgument);
    }

    @Override
    public Double parse(CommandContext ctx) {
        return DoubleArgumentType.getDouble(ctx.getHandle(), name);
    }
}
