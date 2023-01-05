package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class BooleanArgument extends Argument<Boolean> {
    public BooleanArgument(String name) {
        this(name, option -> {
        });
    }

    public BooleanArgument(String name, Consumer<Option<Boolean, CommandContext>> options) {
        super(name, BoolArgumentType.bool());
        setOptions(options);
    }

    @Override
    public Boolean cast(Object parsedArgument) {
        return ((Boolean) parsedArgument);
    }

    @Override
    public Boolean parse(CommandContext ctx) {
        return BoolArgumentType.getBool(ctx.getHandle(), name);
    }
}
