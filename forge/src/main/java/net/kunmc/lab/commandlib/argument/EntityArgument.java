package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;

import java.util.function.Consumer;

public class EntityArgument extends Argument<Entity> {
    public EntityArgument(String name, Consumer<Option<Entity>> options) {
        super(name, net.minecraft.command.arguments.EntityArgument.entity());
        setOptions(options);
    }

    @Override
    public Entity parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return net.minecraft.command.arguments.EntityArgument.getEntity(ctx, name);
    }
}
