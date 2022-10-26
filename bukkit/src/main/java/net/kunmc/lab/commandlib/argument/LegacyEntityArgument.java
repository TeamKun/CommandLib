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

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LegacyEntityArgument extends Argument<List<Entity>> {
    private final boolean enableEntities;
    private final boolean single;

    public LegacyEntityArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction, boolean enableEntities, boolean single) {
        super(name, suggestionAction, contextAction, ((Supplier<ArgumentEntity>) () -> {
            if (enableEntities) {
                if (single) {
                    return ArgumentEntity.a();
                } else {
                    return ArgumentEntity.multipleEntities();
                }
            } else {
                if (single) {
                    return ArgumentEntity.c();
                } else {
                    return ArgumentEntity.d();
                }
            }
        }).get());

        this.enableEntities = enableEntities;
        this.single = single;
    }

    @Override
    protected List<Entity> cast(Object parsedArgument) {
        return ((List<Entity>) parsedArgument);
    }

    @Override
    public List<Entity> parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            if (enableEntities) {
                if (single) {
                    return Collections.singletonList(ArgumentEntity.a(ctx, name).getBukkitEntity());
                } else {
                    return ArgumentEntity.b(ctx, name).stream()
                            .map(net.minecraft.server.v1_16_R3.Entity::getBukkitEntity)
                            .collect(Collectors.toList());
                }
            } else {
                if (single) {
                    return Collections.singletonList(ArgumentEntity.e(ctx, name).getBukkitEntity());
                } else {
                    return ArgumentEntity.f(ctx, name).stream()
                            .map(net.minecraft.server.v1_16_R3.Entity::getBukkitEntity)
                            .collect(Collectors.toList());
                }
            }
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
