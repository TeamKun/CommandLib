package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.ArgumentItemStack;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemStackArgument extends Argument<ItemStack> {
    public ItemStackArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentItemStack.a());
    }

    @Override
    public ItemStack parse(CommandContext<CommandListenerWrapper> ctx) {
        try {
            return CraftItemStack.asCraftMirror(ArgumentItemStack.a(ctx, name).a(1, false));
        } catch (CommandSyntaxException ignored) {
            return null;
        }
    }
}
