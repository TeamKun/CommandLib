package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.LongArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

import java.util.function.Consumer;

public class CommonLongArgument<C extends AbstractCommandContext<?, ?>> extends CommonArgument<Long, C> {
    public CommonLongArgument(String name) {
        this(name, option -> {
        });
    }

    public CommonLongArgument(String name, Consumer<Option<Long, C>> options) {
        super(name, LongArgumentType.longArg(Long.MIN_VALUE, Long.MAX_VALUE));
        applyOptions(options);
    }

    public CommonLongArgument(String name, Long min, Long max) {
        this(name, option -> {
        }, min, max);
    }

    public CommonLongArgument(String name, Consumer<Option<Long, C>> options, Long min, Long max) {
        super(name, LongArgumentType.longArg(min, max));
        applyOptions(options);
    }

    @Override
    public final Long cast(Object parsedArgument) {
        return ((Long) parsedArgument);
    }

    @Override
    protected final Long parseImpl(C ctx) {
        return LongArgumentType.getLong(ctx.getHandle(), name());
    }
}
