package net.kunmc.lab.commandlib.util.nms.server;

import net.kunmc.lab.commandlib.util.nms.CraftBukkitClass;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class NMSCraftServer extends CraftBukkitClass {
    public NMSCraftServer() {
        this(Bukkit.getServer());
    }

    public NMSCraftServer(Server handle) {
        super(handle, "CraftServer");
    }

    public NMSDedicatedServer getServer() {
        return new NMSDedicatedServer(invokeMethod("getServer"));
    }
}
