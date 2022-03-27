package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentMobEffect;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.potion.PotionEffect;

public class PotionEffectArgument extends Argument<PotionEffect> {
    public PotionEffectArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentMobEffect.a());
    }

    @Override
    public PotionEffect parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            return new CraftPotionEffectType(ArgumentMobEffect.a(ctx, name)).createEffect(1, 0);
        } catch (CommandSyntaxException ignored) {
            return null;
        }
    }
}
