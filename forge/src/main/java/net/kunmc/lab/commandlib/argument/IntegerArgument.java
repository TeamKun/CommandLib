package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.minecraft.command.CommandSource;

import java.util.function.Consumer;

public class IntegerArgument extends Argument<Integer> {
    public IntegerArgument(String name, Consumer<Option<Integer>> options) {
        super(name, IntegerArgumentType.integer(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    public IntegerArgument(String name, Consumer<Option<Integer>> options, Integer min, Integer max) {
        super(name, IntegerArgumentType.integer(min, max));
        setOptions(options);
    }

    @Override
    public Integer parse(CommandContext<CommandSource> ctx) {
        return IntegerArgumentType.getInteger(ctx, name);
    }
}
