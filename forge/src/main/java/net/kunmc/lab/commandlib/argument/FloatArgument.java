package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.FloatArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class FloatArgument extends Argument<Float> {
    public FloatArgument(String name) {
        this(name, option -> {
        });
    }

    public FloatArgument(String name, Consumer<Option<Float>> options) {
        super(name, FloatArgumentType.floatArg(-Float.MAX_VALUE, Float.MAX_VALUE));
        setOptions(options);
    }

    public FloatArgument(String name, Float min, Float max) {
        this(name, option -> {
        }, min, max);
    }

    public FloatArgument(String name, Consumer<Option<Float>> options, Float min, Float max) {
        super(name, FloatArgumentType.floatArg(min, max));
        setOptions(options);
    }

    @Override
    public Float cast(Object parsedArgument) {
        return ((Float) parsedArgument);
    }

    @Override
    public Float parse(CommandContext ctx) {
        return FloatArgumentType.getFloat(ctx.getHandle(), name);
    }
}
