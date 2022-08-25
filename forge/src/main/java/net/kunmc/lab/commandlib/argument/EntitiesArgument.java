package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class EntitiesArgument extends Argument<List<Entity>> {
    public EntitiesArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, EntityArgument.entities());
    }

    @Override
    public List<Entity> parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        try {
            return new ArrayList<>(EntityArgument.getEntities(ctx, name));
        } catch (CommandSyntaxException e) {
            throw new IncorrectArgumentInputException(((ITextComponent) e.getRawMessage()));
        }
    }
}
