package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentTile;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

import java.util.function.Consumer;

public class BlockDataArgument extends Argument<BlockData> {
    public BlockDataArgument(String name) {
        this(name, option -> {
        });
    }

    public BlockDataArgument(String name, Consumer<Option<BlockData>> options) {
        super(name, ArgumentTile.a());
        setOptions(options);
    }

    @Override
    public BlockData cast(Object parsedArgument) {
        return ((BlockData) parsedArgument);
    }

    @Override
    public BlockData parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        return CraftBlockData.createData(ArgumentTile.a(ctx, name()).a());
    }
}