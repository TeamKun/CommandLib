package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class ItemStackArgument extends Argument<ItemStack> {
    public ItemStackArgument(String name) {
        this(name, option -> {
        });
    }

    public ItemStackArgument(String name, Consumer<Option<ItemStack, CommandContext>> options) {
        super(name, ItemArgument.item());
        setOptions(options);
    }

    @Override
    public ItemStack cast(Object parsedArgument) {
        return ((ItemStack) parsedArgument);
    }

    @Override
    public ItemStack parse(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return ItemArgument.getItem(ctx.getHandle(), name)
                           .createStack(1, false);
    }
}
