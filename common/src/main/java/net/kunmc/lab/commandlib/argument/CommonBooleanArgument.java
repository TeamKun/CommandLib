package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

import java.util.function.Consumer;

public class CommonBooleanArgument<C extends AbstractCommandContext<?, ?>> extends CommonArgument<Boolean, C> {
    public CommonBooleanArgument(String name) {
        this(name, option -> {
        });
    }

    public CommonBooleanArgument(String name, Consumer<Option<Boolean, C>> options) {
        super(name, BoolArgumentType.bool());
        setOptions(options);
    }

    @Override
    public final Boolean cast(Object parsedArgument) {
        return ((Boolean) parsedArgument);
    }

    @Override
    protected final Boolean parseImpl(C ctx) {
        return BoolArgumentType.getBool(ctx.getHandle(), name);
    }
}
