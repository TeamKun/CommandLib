package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.exception.MethodNotFoundException;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;

public class NMSArgumentItemStack extends NMSArgument<NMSArgumentPredicateItemStack> {
    public NMSArgumentItemStack() {
        super("ArgumentItemStack", "commands.arguments.item.ArgumentItemStack", "commands.arguments.item.ItemArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        try {
            return ((ArgumentType<?>) invokeMethod("a"));
        } catch (MethodNotFoundException e) {
            NMSCommandBuildContext commandBuildContext = new NMSCraftServer().getServer()
                                                                             .getDataPackResources()
                                                                             .getCommandBuildContext();
            return ((ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass()},
                                                  new Object[]{commandBuildContext.getHandle()}));
        }
    }

    @Override
    protected NMSArgumentPredicateItemStack parseImpl(CommandContext<?> ctx, String name) {
        return new NMSArgumentPredicateItemStack(invokeMethod("a", "getItem", ctx, name));
    }
}
