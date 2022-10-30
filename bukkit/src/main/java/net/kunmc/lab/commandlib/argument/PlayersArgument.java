package net.kunmc.lab.commandlib.argument;


import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlayersArgument extends Argument<List<Player>> {
    public PlayersArgument(String name) {
        this(name, option -> {
        });
    }

    public PlayersArgument(String name, Consumer<Option<List<Player>>> options) {
        super(name, ArgumentEntity.d());
        setOptions(options);
    }

    @Override
    public List<Player> cast(Object parsedArgument) {
        return ((List<Player>) parsedArgument);
    }

    @Override
    public List<Player> parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return ArgumentEntity.f(ctx, name).stream()
                .map(net.minecraft.server.v1_16_R3.Entity::getBukkitEntity)
                .map(Player.class::cast)
                .collect(Collectors.toList());
    }
}
