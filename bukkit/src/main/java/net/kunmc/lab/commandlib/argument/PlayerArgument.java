package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Player;

public class PlayerArgument extends Argument<Player> {
    public PlayerArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentEntity.c());
    }

    @Override
    public Player parse(CommandContext<CommandListenerWrapper> ctx) {
        try {
            return ((Player) ArgumentEntity.e(ctx, name).getBukkitEntity());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
