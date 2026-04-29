package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PlayerArgument extends Argument<Player, PlayerArgument> {
    public PlayerArgument(String name) {
        super(name, ArgumentTypes.player());
    }

    @Override
    public Player cast(Object parsedArgument) {
        return (Player) parsedArgument;
    }

    @Override
    protected Player parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        PlayerSelectorArgumentResolver resolver = ctx.getHandle()
                                                     .getArgument(name(), PlayerSelectorArgumentResolver.class);
        List<Player> players = resolver.resolve(ctx.getHandle()
                                                   .getSource());
        if (players.isEmpty()) {
            throw new ArgumentParseException(x -> x.sendFailure("no player found"));
        }
        return players.get(0);
    }
}
