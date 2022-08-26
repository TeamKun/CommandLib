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

import java.util.function.Predicate;

public class EntityArgument extends Argument<Entity> {
    private final Predicate<? super Entity> filter;

    public EntityArgument(String name, SuggestionAction suggestionAction, Predicate<? super Entity> filter, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentEntity.a());
        this.filter = filter;
    }

    @Override
    public Entity parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            Entity entity = ArgumentEntity.a(ctx, name).getBukkitEntity();
            if (filter == null || filter.test(entity)) {
                return entity;
            }
            throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
