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
public class EntityArgument extends Argument<Entity, EntityArgument> {
    public EntityArgument(String name) {
        super(name, ArgumentTypes.entity());
    }

    @Override
    public Entity cast(Object parsedArgument) {
        return (Entity) parsedArgument;
    }

    @Override
    protected Entity parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        EntitySelectorArgumentResolver resolver = ctx.getHandle()
                                                     .getArgument(name(), EntitySelectorArgumentResolver.class);
        List<Entity> entities = resolver.resolve(ctx.getHandle()
                                                    .getSource());
        if (entities.isEmpty()) {
            throw new ArgumentParseException(x -> x.sendFailure("no entity found"));
        }
        return entities.get(0);
    }
}
