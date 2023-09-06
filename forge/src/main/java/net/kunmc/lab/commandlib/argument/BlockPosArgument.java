package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class BlockPosArgument extends Argument<BlockPos> {
    public BlockPosArgument(String name) {
        this(name, option -> {
        });
    }

    public BlockPosArgument(String name, Consumer<Option<BlockPos, CommandContext>> options) {
        super(name, Vec3Argument.vec3());
        applyOptions(options);
    }

    @Override
    public BlockPos cast(Object parsedArgument) {
        return ((BlockPos) parsedArgument);
    }

    @Override
    protected BlockPos parseImpl(CommandContext ctx) throws IncorrectArgumentInputException {
        return Vec3Argument.getLocation(ctx.getHandle(), name())
                           .getBlockPos(ctx.getHandle()
                                           .getSource());
    }
}
