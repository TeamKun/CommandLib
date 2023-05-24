package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentItemStack;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemStackArgument extends Argument<ItemStack> {
    public ItemStackArgument(String name) {
        this(name, option -> {
        });
    }

    public ItemStackArgument(String name, Consumer<Option<ItemStack, CommandContext>> options) {
        super(name, new NMSArgumentItemStack().argument());
        setOptions(options);
    }

    @Override
    public ItemStack cast(Object parsedArgument) {
        return ((ItemStack) parsedArgument);
    }

    @Override
    protected ItemStack parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return new NMSCraftItemStack().asCraftMirror(new NMSArgumentItemStack().parse(ctx.getHandle(), name)
                                                                               .createItemStack(1, false));
    }
}
