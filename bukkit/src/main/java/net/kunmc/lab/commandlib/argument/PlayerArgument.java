package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class PlayerArgument extends Argument<Player> {
    private final Predicate<? super Player> filter;

    public PlayerArgument(String name, SuggestionAction suggestionAction, Predicate<? super Player> filter, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentEntity.c());
        this.filter = filter;
    }

    @Override
    public Player parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            Player p = ((Player) ArgumentEntity.e(ctx, name).getBukkitEntity());
            if (filter == null || filter.test(p)) {
                return p;
            }
            throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
