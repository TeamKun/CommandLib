package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public class EntityArgument extends Argument<Entity> {
    public EntityArgument(String name) {
        this(name, option -> {
        });
    }

    public EntityArgument(String name, Consumer<Option<Entity, CommandContext>> options) {
        super(name, ArgumentEntity.a());
        setOptions(options);
    }

    @Override
    public Entity cast(Object parsedArgument) {
        return ((Entity) parsedArgument);
    }

    @Override
    public Entity parse(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return ArgumentEntity.a(ctx.getHandle(), name)
                             .getBukkitEntity();
    }
}
