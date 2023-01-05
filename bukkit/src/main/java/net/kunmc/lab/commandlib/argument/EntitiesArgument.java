package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EntitiesArgument extends Argument<List<Entity>> {
    public EntitiesArgument(String name) {
        this(name, option -> {
        });
    }

    public EntitiesArgument(String name, Consumer<Option<List<Entity>, CommandContext>> options) {
        super(name, ArgumentEntity.multipleEntities());
        setOptions(options);
    }

    @Override
    public List<Entity> cast(Object parsedArgument) {
        return ((List<Entity>) parsedArgument);
    }

    @Override
    public List<Entity> parse(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return ArgumentEntity.b(ctx.getHandle(), name)
                             .stream()
                             .map(net.minecraft.server.v1_16_R3.Entity::getBukkitEntity)
                             .collect(Collectors.toList());
    }
}
