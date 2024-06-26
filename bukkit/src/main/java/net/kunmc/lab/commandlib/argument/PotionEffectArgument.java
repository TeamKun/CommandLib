package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentMobEffect;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftPotionEffectType;
import org.bukkit.potion.PotionEffect;

import java.util.function.Consumer;

public class PotionEffectArgument extends Argument<PotionEffect> {
    public PotionEffectArgument(String name) {
        this(name, option -> {
        });
    }

    public PotionEffectArgument(String name, Consumer<Option<PotionEffect, CommandContext>> options) {
        super(name,
              NMSArgumentMobEffect.create()
                                  .argument());
        applyOptions(options);
    }

    @Override
    public PotionEffect cast(Object parsedArgument) {
        return ((PotionEffect) parsedArgument);
    }

    @Override
    protected PotionEffect parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return NMSCraftPotionEffectType.create()
                                       .createInstance(NMSArgumentMobEffect.create()
                                                                           .parse(ctx.getHandle(), name()))
                                       .createEffect(1, 0);
    }
}
