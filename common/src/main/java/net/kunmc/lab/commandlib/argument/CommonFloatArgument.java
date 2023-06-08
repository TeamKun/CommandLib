package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.FloatArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

import java.util.function.Consumer;

public class CommonFloatArgument<C extends AbstractCommandContext<?, ?>> extends CommonArgument<Float, C> {
    public CommonFloatArgument(String name) {
        this(name, option -> {
        });
    }

    public CommonFloatArgument(String name, Consumer<Option<Float, C>> options) {
        super(name, FloatArgumentType.floatArg(-Float.MAX_VALUE, Float.MAX_VALUE));
        setOptions(options);
    }

    public CommonFloatArgument(String name, Float min, Float max) {
        this(name, option -> {
        }, min, max);
    }

    public CommonFloatArgument(String name, Consumer<Option<Float, C>> options, Float min, Float max) {
        super(name, FloatArgumentType.floatArg(min, max));
        setOptions(options);
    }

    @Override
    public final Float cast(Object parsedArgument) {
        return ((Float) parsedArgument);
    }

    @Override
    protected final Float parseImpl(C ctx) {
        return FloatArgumentType.getFloat(ctx.getHandle(), name());
    }
}
