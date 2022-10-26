package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EntitiesArgument extends Argument<List<Entity>> {
    public EntitiesArgument(String name) {
        this(name, option -> {
        });
    }

    public EntitiesArgument(String name, Consumer<Option<List<Entity>>> options) {
        super(name, ArgumentEntity.multipleEntities());
        setOptions(options);
    }

    @Override
    protected List<Entity> cast(Object parsedArgument) {
        return ((List<Entity>) parsedArgument);
    }

    @Override
    public List<Entity> parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return ArgumentEntity.b(ctx, name).stream()
                .map(net.minecraft.server.v1_16_R3.Entity::getBukkitEntity)
                .collect(Collectors.toList());
    }
}
