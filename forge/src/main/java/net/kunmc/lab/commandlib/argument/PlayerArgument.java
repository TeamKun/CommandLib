package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.function.Consumer;

public class PlayerArgument extends Argument<ServerPlayerEntity> {
    public PlayerArgument(String name) {
        this(name, option -> {
        });
    }

    public PlayerArgument(String name, Consumer<Option<ServerPlayerEntity, CommandContext>> options) {
        super(name, EntityArgument.player());
        setOptions(options);
    }

    @Override
    public ServerPlayerEntity cast(Object parsedArgument) {
        return ((ServerPlayerEntity) parsedArgument);
    }

    @Override
    public ServerPlayerEntity parse(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return EntityArgument.getPlayer(ctx.getHandle(), name);
    }
}
