package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

import java.util.function.Consumer;

public class CommonIntegerArgument<C extends AbstractCommandContext<?, ?>> extends CommonArgument<Integer, C> {
    public CommonIntegerArgument(String name) {
        this(name, option -> {
        });
    }

    public CommonIntegerArgument(String name, Consumer<Option<Integer, C>> options) {
        super(name, IntegerArgumentType.integer(Integer.MIN_VALUE, Integer.MAX_VALUE));
        setOptions(options);
    }

    public CommonIntegerArgument(String name, Integer min, Integer max) {
        this(name, option -> {
        }, min, max);
    }

    public CommonIntegerArgument(String name, Consumer<Option<Integer, C>> options, Integer min, Integer max) {
        super(name, IntegerArgumentType.integer(min, max));
        setOptions(options);
    }

    @Override
    public final Integer cast(Object parsedArgument) {
        return ((Integer) parsedArgument);
    }

    @Override
    protected final Integer parseImpl(C ctx) {
        return IntegerArgumentType.getInteger(ctx.getHandle(), name());
    }
}
