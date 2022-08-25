package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerArgument extends Argument<PlayerEntity> {
    public PlayerArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, EntityArgument.player());
    }

    @Override
    public PlayerEntity parse(CommandContext<CommandSource> ctx) {
        try {
            return net.minecraft.command.arguments.EntityArgument.getPlayer(ctx, name);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
