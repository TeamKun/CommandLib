package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntitiesArgument extends Argument<List<Entity>> {
    private final Predicate<? super List<Entity>> filter;

    public EntitiesArgument(String name, SuggestionAction suggestionAction, Predicate<? super List<Entity>> filter, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentEntity.multipleEntities());
        this.filter = filter;
    }

    @Override
    public List<Entity> parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            List<Entity> list = ArgumentEntity.b(ctx, name).stream()
                    .map(net.minecraft.server.v1_16_R3.Entity::getBukkitEntity)
                    .collect(Collectors.toList());
            if (filter == null || filter.test(list)) {
                return list;
            }
            throw new IncorrectArgumentInputException(this, ctx, name);
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
