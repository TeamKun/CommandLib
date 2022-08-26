package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEnchantment;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.craftbukkit.v1_16_R3.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;

import java.util.function.Predicate;

public class EnchantmentArgument extends Argument<Enchantment> {
    private final Predicate<? super Enchantment> filter;

    public EnchantmentArgument(String name, SuggestionAction suggestionAction, Predicate<? super Enchantment> filter, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentEnchantment.a());
        this.filter = filter;
    }

    @Override
    public Enchantment parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        Enchantment enchantment = new CraftEnchantment(ArgumentEnchantment.a(ctx, name));
        if (filter == null || filter.test(enchantment)) {
            return enchantment;
        }
        throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
    }
}
