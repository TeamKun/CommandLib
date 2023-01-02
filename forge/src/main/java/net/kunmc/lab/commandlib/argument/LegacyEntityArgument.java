package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class LegacyEntityArgument extends Argument<List<Entity>> {
    private final boolean enableEntities;
    private final boolean single;

    public LegacyEntityArgument(String name,
                                SuggestionAction suggestionAction,
                                ContextAction contextAction,
                                boolean enableEntities,
                                boolean single) {
        super(name, suggestionAction, contextAction, ((Supplier<net.minecraft.command.arguments.EntityArgument>) () -> {
            if (enableEntities) {
                if (single) {
                    return net.minecraft.command.arguments.EntityArgument.entity();
                } else {
                    return net.minecraft.command.arguments.EntityArgument.entities();
                }
            } else {
                if (single) {
                    return net.minecraft.command.arguments.EntityArgument.player();
                } else {
                    return net.minecraft.command.arguments.EntityArgument.players();
                }
            }
        }).get());

        this.enableEntities = enableEntities;
        this.single = single;
    }

    @Override
    public List<Entity> cast(Object parsedArgument) {
        return ((List<Entity>) parsedArgument);
    }

    @Override
    public List<Entity> parse(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        if (enableEntities) {
            if (single) {
                return Collections.singletonList(net.minecraft.command.arguments.EntityArgument.getEntity(ctx.getHandle(),
                                                                                                          name));
            } else {
                return new ArrayList<>(net.minecraft.command.arguments.EntityArgument.getEntities(ctx.getHandle(),
                                                                                                  name));
            }
        } else {
            if (single) {
                return Collections.singletonList(net.minecraft.command.arguments.EntityArgument.getPlayer(ctx.getHandle(),
                                                                                                          name));
            } else {
                return new ArrayList<>(net.minecraft.command.arguments.EntityArgument.getPlayers(ctx.getHandle(),
                                                                                                 name));
            }
        }
    }
}
