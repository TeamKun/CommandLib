package net.kunmc.lab.commandlib.util.nms.server.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import net.kunmc.lab.commandlib.util.nms.server.NMSDedicatedServer;
import org.bukkit.Server;

public class NMSCraftServer_v1_16_0 extends NMSCraftServer {
    public NMSCraftServer_v1_16_0(Server handle) {
        super(handle, "CraftServer");
    }

    @Override
    public NMSDedicatedServer getServer() {
        return NMSDedicatedServer.create(invokeMethod("getServer"));
    }
}
