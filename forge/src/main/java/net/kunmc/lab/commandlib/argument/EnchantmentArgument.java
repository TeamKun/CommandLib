package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.enchantment.Enchantment;

import java.util.function.Consumer;

public class EnchantmentArgument extends Argument<Enchantment> {
    public EnchantmentArgument(String name) {
        this(name, option -> {
        });
    }

    public EnchantmentArgument(String name, Consumer<Option<Enchantment, CommandContext>> options) {
        super(name, net.minecraft.command.arguments.EnchantmentArgument.enchantment());
        applyOptions(options);
    }

    @Override
    public Enchantment cast(Object parsedArgument) {
        return ((Enchantment) parsedArgument);
    }

    @Override
    protected Enchantment parseImpl(CommandContext ctx) throws IncorrectArgumentInputException {
        return net.minecraft.command.arguments.EnchantmentArgument.getEnchantment(ctx.getHandle(), name());
    }
}
