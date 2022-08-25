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

public class EntityArgument extends Argument<Entity> {
    public EntityArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentEntity.a());
    }

    @Override
    public Entity parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            return ArgumentEntity.a(ctx, name).getBukkitEntity();
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
