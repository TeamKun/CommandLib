package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.util.math.BlockPos;

public class BlockPosArgument extends Argument<BlockPos> {
    public BlockPosArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, Vec3Argument.vec3());
    }

    @Override
    public BlockPos parse(CommandContext<CommandSource> ctx) {
        return Vec3Argument.getLocation(ctx, name).getBlockPos(ctx.getSource());
    }
}
