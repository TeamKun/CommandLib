package net.kunmc.lab.commandlib.util.nms.argument.v1_17_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentItemStack;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPredicateItemStack;

public class NMSArgumentItemStack_v1_17_0 extends NMSArgumentItemStack {
    public NMSArgumentItemStack_v1_17_0() {
        super(null, "commands.arguments.item.ArgumentItemStack");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("a"));
    }

    @Override
    protected NMSArgumentPredicateItemStack parseImpl(CommandContext<?> ctx, String name) {
        return NMSArgumentPredicateItemStack.create(invokeStaticMethod("a", ctx, name));
    }
}
