package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentItemStack;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPredicateItemStack;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;

public class NMSArgumentItemStack_v1_20_5 extends NMSArgumentItemStack {
    public NMSArgumentItemStack_v1_20_5() {
        super(null, "commands.arguments.item.ItemArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        NMSCommandBuildContext commandBuildContext = NMSCraftServer.create()
                                                                   .getServer()
                                                                   .getDataPackResources()
                                                                   .getCommandBuildContext();
        return ((ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass()},
                                              new Object[]{commandBuildContext.getHandle()}));
    }

    @Override
    protected NMSArgumentPredicateItemStack parseImpl(CommandContext<?> ctx, String name) {
        return NMSArgumentPredicateItemStack.create(invokeStaticMethod("getItem", ctx, name));
    }
}
