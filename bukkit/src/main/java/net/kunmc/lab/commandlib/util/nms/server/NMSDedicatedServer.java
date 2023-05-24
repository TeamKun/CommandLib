package net.kunmc.lab.commandlib.util.nms.server;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;

public class NMSDedicatedServer extends MinecraftClass {
    public NMSDedicatedServer(Object handle) {
        super(handle, "DedicatedServer", "server.dedicated.DedicatedServer");
    }

    public NMSCommandDispatcher getCommandDispatcher() {
        return new NMSCommandDispatcher(invokeMethod("getCommandDispatcher", "getCommands", "aC"));
    }

    public NMSDataPackResources getDataPackResources() {
        Object obj = getValue(Object.class, "dataPackResources", "au", "resources");
        try {
            return new NMSDataPackResources(obj);
        } catch (Exception e) {
            return new NMSReloadableResources(obj).getDataPackResources();
        }
    }

    public static class NMSReloadableResources extends MinecraftClass {
        public NMSReloadableResources(Object handle) {
            super(handle, "server.MinecraftServer$ReloadableResources");
        }

        public NMSDataPackResources getDataPackResources() {
            return new NMSDataPackResources(invokeMethod("b", "managers"));
        }
    }
}
