package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class PlayerArgument extends Argument<Player> {
    public PlayerArgument(String name) {
        this(name, option -> {
        });
    }

    public PlayerArgument(String name, Consumer<Option<Player, CommandContext>> options) {
        super(name, ArgumentEntity.c());
        setOptions(options);
    }

    @Override
    public Player cast(Object parsedArgument) {
        return ((Player) parsedArgument);
    }

    @Override
    protected Player parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return ((Player) ArgumentEntity.e(ctx.getHandle(), name)
                                       .getBukkitEntity());
    }
}
