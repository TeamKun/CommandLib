package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

import java.util.function.Consumer;

public class CommonDoubleArgument<C extends AbstractCommandContext<?, ?>> extends CommonArgument<Double, C> {
    public CommonDoubleArgument(String name) {
        this(name, option -> {
        });
    }

    public CommonDoubleArgument(String name, Consumer<Option<Double, C>> options) {
        super(name, DoubleArgumentType.doubleArg(-Double.MAX_VALUE, Double.MAX_VALUE));
        applyOptions(options);
    }

    public CommonDoubleArgument(String name, Double min, Double max) {
        this(name, option -> {
        }, min, max);
    }

    public CommonDoubleArgument(String name, Consumer<Option<Double, C>> options, Double min, Double max) {
        super(name, DoubleArgumentType.doubleArg(min, max));
        applyOptions(options);
    }

    @Override
    public final Double cast(Object parsedArgument) {
        return ((Double) parsedArgument);
    }

    @Override
    protected final Double parseImpl(C ctx) {
        return DoubleArgumentType.getDouble(ctx.getHandle(), name());
    }
}
