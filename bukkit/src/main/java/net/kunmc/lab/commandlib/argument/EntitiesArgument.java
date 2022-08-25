package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.stream.Collectors;

public class EntitiesArgument extends Argument<List<Entity>> {
    public EntitiesArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentEntity.multipleEntities());
    }

    @Override
    public List<Entity> parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            return ArgumentEntity.b(ctx, name).stream()
                    .map(net.minecraft.server.v1_16_R3.Entity::getBukkitEntity)
                    .collect(Collectors.toList());
        } catch (CommandSyntaxException e) {
            System.out.println(((ChatMessage) e.getRawMessage()).getKey());
            throw new IncorrectArgumentInputException(Component.translatable(((ChatMessage) e.getRawMessage()).getKey()));
        }
    }
}
