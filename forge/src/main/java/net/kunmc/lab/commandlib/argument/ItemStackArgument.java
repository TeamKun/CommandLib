package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class ItemStackArgument extends Argument<ItemStack> {
    public ItemStackArgument(String name) {
        this(name, option -> {
        });
    }

    public ItemStackArgument(String name, Consumer<Option<ItemStack>> options) {
        super(name, net.minecraft.command.arguments.ItemArgument.item());
        setOptions(options);
    }

    @Override
    public ItemStack cast(Object parsedArgument) {
        return ((ItemStack) parsedArgument);
    }

    @Override
    public ItemStack parse(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return net.minecraft.command.arguments.ItemArgument.getItem(ctx.getHandle(), name)
                                                           .createStack(1, false);
    }
}
