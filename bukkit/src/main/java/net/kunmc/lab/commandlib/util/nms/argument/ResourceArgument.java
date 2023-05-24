package net.kunmc.lab.commandlib.util.nms.argument;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;

// 1.16.5には存在しない
public class ResourceArgument extends MinecraftClass {
    public static ResourceArgument createInstance(NMSResourceKey resourceKey) {
        NMSCommandBuildContext commandBuildContext = new NMSCraftServer().getServer()
                                                                         .getDataPackResources()
                                                                         .getCommandBuildContext();

        return new ResourceArgument(new ResourceArgument(null).newInstance(new Class[]{commandBuildContext.getFoundClass(), resourceKey.getFoundClass()},
                                                                           new Object[]{commandBuildContext.getHandle(), resourceKey.getHandle()}));
    }

    public ResourceArgument(Object handle) {
        super(handle, "commands.arguments.ResourceArgument");
    }
}
