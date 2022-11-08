package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentItemStack;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemStackArgument extends Argument<ItemStack> {
    public ItemStackArgument(String name) {
        this(name, option -> {
        });
    }

    public ItemStackArgument(String name, Consumer<Option<ItemStack>> options) {
        super(name, ArgumentItemStack.a());
        setOptions(options);
    }

    @Override
    public ItemStack cast(Object parsedArgument) {
        return ((ItemStack) parsedArgument);
    }

    @Override
    public ItemStack parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        return CraftItemStack.asCraftMirror(ArgumentItemStack.a(ctx, name)
                                                             .a(1, false));
    }
}
