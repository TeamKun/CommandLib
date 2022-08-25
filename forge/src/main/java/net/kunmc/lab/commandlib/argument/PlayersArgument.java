package net.kunmc.lab.commandlib.argument;


import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayersArgument extends Argument<List<PlayerEntity>> {
    public PlayersArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, EntityArgument.players());
    }

    @Override
    public List<PlayerEntity> parse(CommandContext<CommandSource> ctx) {
        try {
            return new ArrayList<>(EntityArgument.getPlayers(ctx, name));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
