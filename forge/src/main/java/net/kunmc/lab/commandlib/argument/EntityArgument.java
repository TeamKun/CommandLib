package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;

public class EntityArgument extends Argument<Entity> {
    public EntityArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, net.minecraft.command.arguments.EntityArgument.entity());
    }

    @Override
    public Entity parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        try {
            return net.minecraft.command.arguments.EntityArgument.getEntity(ctx, name);
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }

    }
}
