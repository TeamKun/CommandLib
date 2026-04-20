package net.kunmc.lab.commandlib.util.nms.server.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.server.NMSDataPackResources;
import net.kunmc.lab.commandlib.util.nms.server.NMSDedicatedServer;

public class NMSDedicatedServer_v1_16_0 extends NMSDedicatedServer {
    public NMSDedicatedServer_v1_16_0(Object handle) {
        super(handle, "DedicatedServer", "server.dedicated.DedicatedServer");
    }

    @Override
    public NMSCommandDispatcher getCommandDispatcher() {
        return NMSCommandDispatcher.create(invokeMethod("getCommandDispatcher"));
    }

    @Override
    public NMSDataPackResources getDataPackResources() {
        Object obj = getValue(Object.class, "dataPackResources");

        return NMSDataPackResources.create(obj);
    }
}
