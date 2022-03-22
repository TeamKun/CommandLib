package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;

public class ItemArgument extends Argument<Item> {
    public ItemArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, net.minecraft.command.arguments.ItemArgument.item());
    }

    @Override
    public Item parse(CommandContext<CommandSource> ctx) {
        return net.minecraft.command.arguments.ItemArgument.getItem(ctx, name).getItem();
    }
}