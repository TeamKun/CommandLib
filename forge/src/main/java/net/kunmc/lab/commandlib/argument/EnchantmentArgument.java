package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
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
    protected Enchantment cast(Object parsedArgument) {
        return ((Enchantment) parsedArgument);
    }

    @Override
    public Enchantment parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        return net.minecraft.command.arguments.EnchantmentArgument.getEnchantment(ctx, name);
    }
}
