package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntitiesArgument extends Argument<List<Entity>> {
    public EntitiesArgument(String name) {
        this(name, option -> {
        });
    }

    public EntitiesArgument(String name, Consumer<Option<List<Entity>>> options) {
        super(name, EntityArgument.entities());
        setOptions(options);
    }

    @Override
    public List<Entity> cast(Object parsedArgument) {
        return ((List<Entity>) parsedArgument);
    }

    @Override
    public List<Entity> parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return new ArrayList<>(EntityArgument.getEntities(ctx, name));
    }
}
