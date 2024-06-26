package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEnchantment;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.util.function.Consumer;

public class EnchantmentArgument extends Argument<Enchantment> {
    public EnchantmentArgument(String name) {
        this(name, option -> {
        });
    }

    public EnchantmentArgument(String name, Consumer<Option<Enchantment, CommandContext>> options) {
        super(name,
              NMSArgumentEnchantment.create()
                                    .argument());
        applyOptions(options);
    }

    @Override
    public Enchantment cast(Object parsedArgument) {
        return ((Enchantment) parsedArgument);
    }

    @Override
    protected Enchantment parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return NMSCraftEnchantment.create()
                                  .createInstance(NMSArgumentEnchantment.create()
                                                                        .parse(ctx.getHandle(), name()));
    }
}
