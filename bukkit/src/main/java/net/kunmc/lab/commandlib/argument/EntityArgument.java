package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEntity;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public class EntityArgument extends Argument<Entity> {
    public EntityArgument(String name) {
        this(name, option -> {
        });
    }

    public EntityArgument(String name, Consumer<Option<Entity, CommandContext>> options) {
        super(name, new NMSArgumentEntity().entityArgument());
        setOptions(options);
    }

    @Override
    public Entity cast(Object parsedArgument) {
        return ((Entity) parsedArgument);
    }

    @Override
    protected Entity parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return new NMSArgumentEntity().getEntity(ctx.getHandle(), name);
    }
}
