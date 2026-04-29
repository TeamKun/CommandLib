package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.entity.Entity;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class EntitiesArgument extends Argument<List<Entity>, EntitiesArgument> {
    public EntitiesArgument(String name) {
        super(name, ArgumentTypes.entities());
    }

    @Override
    public List<Entity> cast(Object parsedArgument) {
        return (List<Entity>) parsedArgument;
    }

    @Override
    protected List<Entity> parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        EntitySelectorArgumentResolver resolver = ctx.getHandle()
                                                     .getArgument(name(), EntitySelectorArgumentResolver.class);
        return resolver.resolve(ctx.getHandle()
                                   .getSource());
    }
}
