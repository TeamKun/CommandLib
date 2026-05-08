package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.kunmc.lab.commandlib.CommonCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

public class CommonBooleanArgument<C extends CommonCommandContext<?, ?>, SELF extends CommonBooleanArgument<C, SELF>> extends CommonArgument<Boolean, C, SELF> {
    public CommonBooleanArgument(String name) {
        super(name, BoolArgumentType.bool());
    }

    @Override
    public final Boolean cast(Object parsedArgument) {
        return ((Boolean) parsedArgument);
    }

    @Override
    protected final Boolean parseImpl(C ctx) {
        return BoolArgumentType.getBool(ctx.getHandle(), name());
    }
}
