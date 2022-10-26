package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.function.Consumer;

public class IntegerArgument extends Argument<Integer> {
    public IntegerArgument(String name) {
        this(name, option -> {
        });
    }

    public IntegerArgument(String name, Consumer<Option<Integer>> options) {
        super(name, IntegerArgumentType.integer(Integer.MIN_VALUE, Integer.MAX_VALUE));
        setOptions(options);
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
    protected Integer cast(Object parsedArgument) {
        return ((Integer) parsedArgument);
    }

    @Override
    public Integer parse(CommandContext<CommandListenerWrapper> ctx) {
        return IntegerArgumentType.getInteger(ctx, name);
    }
}
