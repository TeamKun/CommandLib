package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentMobEffect;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftPotionEffectType;
import org.bukkit.potion.PotionEffect;

public class PotionEffectArgument extends Argument<PotionEffect, PotionEffectArgument> {
    public PotionEffectArgument(String name) {
        super(name,
              NMSArgumentMobEffect.create()
                                  .argument());
    }

    @Override
    public PotionEffect cast(Object parsedArgument) {
        return ((PotionEffect) parsedArgument);
    }

    @Override
    protected PotionEffect parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return NMSCraftPotionEffectType.create()
                                       .createInstance(NMSArgumentMobEffect.create()
                                                                           .parse(ctx.getHandle(), name()))
                                       .createEffect(1, 0);
    }
}
