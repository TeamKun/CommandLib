package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.BlockStateInput;

import java.util.function.Consumer;

public class BlockStateArgument extends Argument<BlockStateInput> {
    public BlockStateArgument(String name) {
        this(name, option -> {
        });
    }

    public BlockStateArgument(String name, Consumer<Option<BlockStateInput>> options) {
        super(name, net.minecraft.command.arguments.BlockStateArgument.blockState());
        setOptions(options);
    }

    @Override
    protected BlockStateInput cast(Object parsedArgument) {
        return ((BlockStateInput) parsedArgument);
    }

    @Override
    public BlockStateInput parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        return net.minecraft.command.arguments.BlockStateArgument.getBlockState(ctx, name);
    }
}