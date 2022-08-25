package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;

public class ItemStackArgument extends Argument<ItemStack> {
    public ItemStackArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, net.minecraft.command.arguments.ItemArgument.item());
    }

    @Override
    public ItemStack parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        try {
            return net.minecraft.command.arguments.ItemArgument.getItem(ctx, name).createStack(1, false);
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
