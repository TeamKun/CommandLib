package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPlayer;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class PlayerArgument extends Argument<Player> {
    public PlayerArgument(String name) {
        this(name, option -> {
        });
    }

    public PlayerArgument(String name, Consumer<Option<Player, CommandContext>> options) {
        super(name, new NMSArgumentPlayer().argument());
        setOptions(options);
    }

    @Override
    public Player cast(Object parsedArgument) {
        return ((Player) parsedArgument);
    }

    @Override
    protected Player parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return new NMSArgumentPlayer().parse(ctx.getHandle(), name());
    }
}
