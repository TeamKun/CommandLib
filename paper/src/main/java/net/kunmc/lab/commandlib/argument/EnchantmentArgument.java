package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.enchantments.Enchantment;

@SuppressWarnings("UnstableApiUsage")
public class EnchantmentArgument extends Argument<Enchantment, EnchantmentArgument> {
    public EnchantmentArgument(String name) {
        super(name, ArgumentTypes.resource(RegistryKey.ENCHANTMENT));
    }

    @Override
    public Enchantment cast(Object parsedArgument) {
        return (Enchantment) parsedArgument;
    }

    @Override
    protected Enchantment parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return ctx.getHandle()
                  .getArgument(name(), Enchantment.class);
    }
}
