package net.kunmc.lab.commandlib.argument;


import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayersArgument extends Argument<List<ServerPlayerEntity>> {
    public PlayersArgument(String name) {
        this(name, option -> {
        });
    }

    public PlayersArgument(String name, Consumer<Option<List<ServerPlayerEntity>>> options) {
        super(name, EntityArgument.players());
        setOptions(options);
    }

    @Override
    public List<ServerPlayerEntity> cast(Object parsedArgument) {
        return ((List<ServerPlayerEntity>) parsedArgument);
    }

    @Override
    public List<ServerPlayerEntity> parse(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return new ArrayList<>(EntityArgument.getPlayers(ctx.getHandle(), name));
    }
}
