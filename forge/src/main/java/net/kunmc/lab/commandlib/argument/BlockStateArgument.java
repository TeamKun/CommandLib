package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.BlockStateInput;

public class BlockStateArgument extends Argument<BlockStateInput> {
    public BlockStateArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, net.minecraft.command.arguments.BlockStateArgument.blockState());
    }

    @Override
    public BlockStateInput parse(CommandContext<CommandSource> ctx) {
        return net.minecraft.command.arguments.BlockStateArgument.getBlockState(ctx, name);
    }
}