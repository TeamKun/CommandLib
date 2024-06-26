package net.kunmc.lab.commandlib.argument;


import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPlayers;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class PlayersArgument extends Argument<List<Player>> {
    public PlayersArgument(String name) {
        this(name, option -> {
        });
    }

    public PlayersArgument(String name, Consumer<Option<List<Player>, CommandContext>> options) {
        super(name,
              NMSArgumentPlayers.create()
                                .argument());
        applyOptions(options);
    }

    @Override
    public List<Player> cast(Object parsedArgument) {
        return ((List<Player>) parsedArgument);
    }

    @Override
    protected List<Player> parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return NMSArgumentPlayers.create()
                                 .parse(ctx.getHandle(), name());
    }
}
