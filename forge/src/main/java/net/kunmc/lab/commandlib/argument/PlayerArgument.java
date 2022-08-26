package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.function.Consumer;

public class PlayerArgument extends Argument<ServerPlayerEntity> {
    public PlayerArgument(String name, Consumer<Option<ServerPlayerEntity>> options) {
        super(name, EntityArgument.player());
        setOptions(options);
    }

    @Override
    public ServerPlayerEntity parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return EntityArgument.getPlayer(ctx, name);
    }
}