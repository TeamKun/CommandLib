package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.ArgumentTile;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

public class BlockDataArgument extends Argument<BlockData> {
    public BlockDataArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentTile.a());
    }

    @Override
    public BlockData parse(CommandContext<CommandListenerWrapper> ctx) {
        return CraftBlockData.createData(ArgumentTile.a(ctx, name).a());
    }
}