package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class IntegerArgument extends Argument<Integer> {
    public IntegerArgument(String name) {
        this(name, option -> {
        });
    }

    public IntegerArgument(String name, Consumer<Option<Integer>> options) {
        super(name, IntegerArgumentType.integer(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    public IntegerArgument(String name, Integer min, Integer max) {
        this(name, option -> {
        }, min, max);
    }

    public IntegerArgument(String name, Consumer<Option<Integer>> options, Integer min, Integer max) {
        super(name, IntegerArgumentType.integer(min, max));
        setOptions(options);
    }

    @Override
    public Integer cast(Object parsedArgument) {
        return ((Integer) parsedArgument);
    }

    @Override
    public Integer parse(CommandContext ctx) {
        return IntegerArgumentType.getInteger(ctx.getHandle(), name);
    }
}
