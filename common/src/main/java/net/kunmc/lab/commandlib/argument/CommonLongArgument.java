package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.LongArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

public class CommonLongArgument<C extends AbstractCommandContext<?, ?>, SELF extends CommonLongArgument<C, SELF>> extends CommonArgument<Long, C, SELF> {
    public CommonLongArgument(String name) {
        super(name, LongArgumentType.longArg(Long.MIN_VALUE, Long.MAX_VALUE));
    }

    public CommonLongArgument(String name, Long min, Long max) {
        super(name, LongArgumentType.longArg(min, max));
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
