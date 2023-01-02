package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEnchantment;
import org.bukkit.craftbukkit.v1_16_R3.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.util.function.Consumer;

public class EnchantmentArgument extends Argument<Enchantment> {
    public EnchantmentArgument(String name) {
        this(name, option -> {
        });
    }

    public EnchantmentArgument(String name, Consumer<Option<Enchantment>> options) {
        super(name, ArgumentEnchantment.a());
        setOptions(options);
    }

    @Override
    public Enchantment cast(Object parsedArgument) {
        return ((Enchantment) parsedArgument);
    }

    @Override
    public Enchantment parse(CommandContext ctx) throws IncorrectArgumentInputException {
        return new CraftEnchantment(ArgumentEnchantment.a(ctx.getHandle(), name));
    }
}
