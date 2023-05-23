package net.kunmc.lab.commandlib.util.nms.server;

import net.kunmc.lab.commandlib.util.nms.CraftBukkitClass;

public class NMSCraftServer extends CraftBukkitClass {
    public NMSCraftServer(Object handle) {
        super(handle, "CraftServer");
    }

    public NMSDedicatedServer getServer() {
        return new NMSDedicatedServer(invokeMethod("getServer"));
    }
}
