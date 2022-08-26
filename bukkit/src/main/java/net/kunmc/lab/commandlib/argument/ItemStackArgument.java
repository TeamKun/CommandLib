package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentItemStack;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class ItemStackArgument extends Argument<ItemStack> {
    private final Predicate<? super ItemStack> filter;

    public ItemStackArgument(String name, SuggestionAction suggestionAction, Predicate<? super ItemStack> filter, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentItemStack.a());
        this.filter = filter;
    }

    @Override
    public ItemStack parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        ItemStack itemStack = CraftItemStack.asCraftMirror(ArgumentItemStack.a(ctx, name).a(1, false));
        if (filter == null || filter.test(itemStack)) {
            return itemStack;
        }
        throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
    }
}
