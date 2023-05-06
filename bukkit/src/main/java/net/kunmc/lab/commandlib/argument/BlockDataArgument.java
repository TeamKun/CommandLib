package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentTile;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

import java.util.function.Consumer;

public class BlockDataArgument extends Argument<BlockData> {
    public BlockDataArgument(String name) {
        this(name, option -> {
        });
    }

    public BlockDataArgument(String name, Consumer<Option<BlockData, CommandContext>> options) {
        super(name, ArgumentTile.a());
        setOptions(options);
    }

    @Override
    public BlockData cast(Object parsedArgument) {
        return ((BlockData) parsedArgument);
    }

    @Override
    protected BlockData parseImpl(CommandContext ctx) throws IncorrectArgumentInputException {
        return CraftBlockData.createData(ArgumentTile.a(ctx.getHandle(), name())
                                                     .a());
    }
}
