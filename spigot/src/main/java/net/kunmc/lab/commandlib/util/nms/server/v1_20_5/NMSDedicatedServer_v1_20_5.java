package net.kunmc.lab.commandlib.util.nms.server.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.server.NMSDataPackResources;
import net.kunmc.lab.commandlib.util.nms.server.NMSDedicatedServer;

public class NMSDedicatedServer_v1_20_5 extends NMSDedicatedServer {
    public NMSDedicatedServer_v1_20_5(Object handle) {
        super(handle, "server.dedicated.DedicatedServer");
    }

    @Override
    public NMSCommandDispatcher getCommandDispatcher() {
        return NMSCommandDispatcher.create(invokeMethod("getCommands"));
    }

    @Override
    public NMSDataPackResources getDataPackResources() {
        Object obj = getValue(Object.class, "resources");

        return NMSReloadableResources.create(obj)
                                     .getDataPackResources();
    }

    public static class NMSReloadableResources_v1_20_5 extends NMSReloadableResources {
        public NMSReloadableResources_v1_20_5(Object handle) {
            super(handle, "server.MinecraftServer$ReloadableResources");
        }

        @Override
        public NMSDataPackResources getDataPackResources() {
            return NMSDataPackResources.create(invokeMethod("managers"));
        }
    }
}
