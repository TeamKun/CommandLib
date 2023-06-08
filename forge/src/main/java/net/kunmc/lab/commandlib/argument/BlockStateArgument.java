package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.arguments.BlockStateInput;

import java.util.function.Consumer;

public class BlockStateArgument extends Argument<BlockStateInput> {
    public BlockStateArgument(String name) {
        this(name, option -> {
        });
    }

    public BlockStateArgument(String name, Consumer<Option<BlockStateInput, CommandContext>> options) {
        super(name, net.minecraft.command.arguments.BlockStateArgument.blockState());
        setOptions(options);
    }

    @Override
    public BlockStateInput cast(Object parsedArgument) {
        return ((BlockStateInput) parsedArgument);
    }

    @Override
    protected BlockStateInput parseImpl(CommandContext ctx) throws IncorrectArgumentInputException {
        return net.minecraft.command.arguments.BlockStateArgument.getBlockState(ctx.getHandle(), name());
    }
}
