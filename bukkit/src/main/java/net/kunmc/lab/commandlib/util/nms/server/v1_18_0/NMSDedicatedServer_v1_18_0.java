package net.kunmc.lab.commandlib.util.nms.server.v1_18_0;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.server.NMSDataPackResources;
import net.kunmc.lab.commandlib.util.nms.server.NMSDedicatedServer;

public class NMSDedicatedServer_v1_18_0 extends NMSDedicatedServer {
    public NMSDedicatedServer_v1_18_0(Object handle) {
        super(handle, "server.dedicated.DedicatedServer");
    }

    @Override
    public NMSCommandDispatcher getCommandDispatcher() {
        return NMSCommandDispatcher.create(invokeMethod("aA"));
    }

    @Override
    public NMSDataPackResources getDataPackResources() {
        Object obj = getValue(Object.class, "aB");

        return NMSDataPackResources.create(obj);
    }
}
