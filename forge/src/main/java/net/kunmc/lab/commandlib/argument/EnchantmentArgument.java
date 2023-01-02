package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.enchantment.Enchantment;

import java.util.function.Consumer;

public class EnchantmentArgument extends Argument<Enchantment> {
    public EnchantmentArgument(String name) {
        this(name, option -> {
        });
    }

    public EnchantmentArgument(String name, Consumer<Option<Enchantment>> options) {
        super(name, net.minecraft.command.arguments.EnchantmentArgument.enchantment());
        setOptions(options);
    }

    @Override
    public Enchantment cast(Object parsedArgument) {
        return ((Enchantment) parsedArgument);
    }

    @Override
    public Enchantment parse(CommandContext ctx) throws IncorrectArgumentInputException {
        return net.minecraft.command.arguments.EnchantmentArgument.getEnchantment(ctx.getHandle(), name);
    }
}
