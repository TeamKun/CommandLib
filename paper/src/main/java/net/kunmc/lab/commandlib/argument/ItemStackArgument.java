package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public class ItemStackArgument extends Argument<ItemStack, ItemStackArgument> {
    public ItemStackArgument(String name) {
        super(name, ArgumentTypes.itemStack());
    }

    @Override
    public ItemStack cast(Object parsedArgument) {
        return (ItemStack) parsedArgument;
    }

    @Override
    protected ItemStack parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return ctx.getHandle()
                  .getArgument(name(), ItemStack.class);
    }
}
