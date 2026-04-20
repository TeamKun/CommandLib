package net.kunmc.lab.commandlib.util.nms.server.v1_19_0;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.server.NMSDataPackResources;
import net.kunmc.lab.commandlib.util.nms.server.NMSDedicatedServer;

public class NMSDedicatedServer_v1_19_0 extends NMSDedicatedServer {
    public NMSDedicatedServer_v1_19_0(Object handle) {
        super(handle, "server.dedicated.DedicatedServer");
    }

    @Override
    public NMSCommandDispatcher getCommandDispatcher() {
        return NMSCommandDispatcher.create(invokeMethod("getCommands", "aC"));
    }

    @Override
    public NMSDataPackResources getDataPackResources() {
        Object obj = getValue(Object.class, "au", "resources");

        return NMSReloadableResources.create(obj)
                                     .getDataPackResources();
    }

    public static class NMSReloadableResources_v1_19_0 extends NMSDedicatedServer.NMSReloadableResources {
        public NMSReloadableResources_v1_19_0(Object handle) {
            super(handle, "server.MinecraftServer$ReloadableResources");
        }

        @Override
        public NMSDataPackResources getDataPackResources() {
            return NMSDataPackResources.create(invokeMethod("b", "managers"));
        }
    }
}
