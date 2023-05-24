package net.kunmc.lab.commandlib.util.nms.server;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;

public class NMSDataPackResources extends MinecraftClass {
    public NMSDataPackResources(Object handle) {
        super(handle, "DataPackResources", "server.DataPackResources", "server.ReloadableServerResources");
    }

    public NMSCommandBuildContext getCommandBuildContext() {
        return new NMSCommandBuildContext(getValue(Object.class, "c"));
    }
}
