package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPlayer;
import org.bukkit.entity.Player;

public class PlayerArgument extends Argument<Player, PlayerArgument> {
    public PlayerArgument(String name) {
        super(name,
              NMSArgumentPlayer.create()
                               .argument());
    }

    @Override
    public Player cast(Object parsedArgument) {
        return ((Player) parsedArgument);
    }

    @Override
    protected Player parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return NMSArgumentPlayer.create()
                                .parse(ctx.getHandle(), name());
    }
}
