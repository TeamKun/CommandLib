package net.kunmc.lab.commandlib.util.nms.server;

import net.kunmc.lab.commandlib.util.bukkit.VersionUtil;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;

public class NMSDedicatedServer extends MinecraftClass {
    public NMSDedicatedServer(Object handle) {
        super(handle, "DedicatedServer", "server.dedicated.DedicatedServer");
    }

    public NMSCommandDispatcher getCommandDispatcher() {
        if (VersionUtil.is1_20_x()) {
            return new NMSCommandDispatcher(invokeMethod("aE"));
        }

        return new NMSCommandDispatcher(invokeMethod("getCommandDispatcher", "getCommands", "aC"));
    }

    public NMSDataPackResources getDataPackResources() {
        Object obj;
        if (VersionUtil.is1_20_x()) {
            obj = getValue(Object.class, "ax");
        } else {
            obj = getValue(Object.class, "dataPackResources", "au", "resources");
        }

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
