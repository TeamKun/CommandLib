package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("UnstableApiUsage")
public class PotionEffectArgument extends Argument<PotionEffect, PotionEffectArgument> {
    public PotionEffectArgument(String name) {
        super(name, ArgumentTypes.resource(RegistryKey.MOB_EFFECT));
    }

    @Override
    public PotionEffect cast(Object parsedArgument) {
        return (PotionEffect) parsedArgument;
    }

    @Override
    protected PotionEffect parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        PotionEffectType type = ctx.getHandle()
                                   .getArgument(name(), PotionEffectType.class);
        return type.createEffect(1, 0);
    }
}
