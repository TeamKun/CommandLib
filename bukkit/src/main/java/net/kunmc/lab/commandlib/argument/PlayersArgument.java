package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPlayers;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayersArgument extends Argument<List<Player>, PlayersArgument> {
    public PlayersArgument(String name) {
        super(name,
              NMSArgumentPlayers.create()
                                .argument());
    }

    @Override
    public List<Player> cast(Object parsedArgument) {
        return ((List<Player>) parsedArgument);
    }

    @Override
    protected List<Player> parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return NMSArgumentPlayers.create()
                                 .parse(ctx.getHandle(), name());
    }
}
