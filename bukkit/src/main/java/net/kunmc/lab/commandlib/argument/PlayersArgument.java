package net.kunmc.lab.commandlib.argument;


import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayersArgument extends Argument<List<Player>> {
    public PlayersArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentEntity.d());
    }

    @Override
    public List<Player> parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            return ArgumentEntity.f(ctx, name).stream()
                    .map(net.minecraft.server.v1_16_R3.Entity::getBukkitEntity)
                    .map(Player.class::cast)
                    .collect(Collectors.toList());
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
