package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentMobEffect;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.potion.PotionEffect;

import java.util.function.Consumer;

public class PotionEffectArgument extends Argument<PotionEffect> {
    public PotionEffectArgument(String name) {
        this(name, option -> {
        });
    }
   
    public PotionEffectArgument(String name, Consumer<Option<PotionEffect>> options) {
        super(name, ArgumentMobEffect.a());
        setOptions(options);
    }

    @Override
    protected PotionEffect cast(Object parsedArgument) {
        return ((PotionEffect) parsedArgument);
    }

    @Override
    public PotionEffect parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return new CraftPotionEffectType(ArgumentMobEffect.a(ctx, name)).createEffect(1, 0);
    }
}
