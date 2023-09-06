package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTile;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftBlockData;
import org.bukkit.block.data.BlockData;

import java.util.function.Consumer;

public class BlockDataArgument extends Argument<BlockData> {
    public BlockDataArgument(String name) {
        this(name, option -> {
        });
    }

    public BlockDataArgument(String name, Consumer<Option<BlockData, CommandContext>> options) {
        super(name, new NMSArgumentTile().argument());
        applyOptions(options);
    }

    @Override
    public BlockData cast(Object parsedArgument) {
        return ((BlockData) parsedArgument);
    }

    @Override
    protected BlockData parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return new NMSCraftBlockData().createData(new NMSArgumentTile().parse(ctx.getHandle(), name())
                                                                       .getBlockData());
    }
}
