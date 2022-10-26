package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public class EntityArgument extends Argument<Entity> {
    public EntityArgument(String name) {
        this(name, option -> {
        });
    }

    public EntityArgument(String name, Consumer<Option<Entity>> options) {
        super(name, ArgumentEntity.a());
        setOptions(options);
    }

    @Override
    protected Entity cast(Object parsedArgument) {
        return ((Entity) parsedArgument);
    }

    @Override
    public Entity parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return ArgumentEntity.a(ctx, name).getBukkitEntity();
    }
}
