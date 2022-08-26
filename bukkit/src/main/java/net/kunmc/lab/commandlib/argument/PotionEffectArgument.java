package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentMobEffect;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.potion.PotionEffect;

import java.util.function.Predicate;

public class PotionEffectArgument extends Argument<PotionEffect> {
    private final Predicate<? super PotionEffect> filter;

    public PotionEffectArgument(String name, SuggestionAction suggestionAction, Predicate<? super PotionEffect> filter, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentMobEffect.a());
        this.filter = filter;
    }

    @Override
    public PotionEffect parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            PotionEffect potionEffect = new CraftPotionEffectType(ArgumentMobEffect.a(ctx, name)).createEffect(1, 0);
            if (filter == null || filter.test(potionEffect)) {
                return potionEffect;
            }
            throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
