package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentArgument extends Argument<Enchantment> {
    public EnchantmentArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, net.minecraft.command.arguments.EnchantmentArgument.enchantment());
    }

    @Override
    public Enchantment parse(CommandContext<CommandSource> ctx) {
        return net.minecraft.command.arguments.EnchantmentArgument.getEnchantment(ctx, name);
    }
}
