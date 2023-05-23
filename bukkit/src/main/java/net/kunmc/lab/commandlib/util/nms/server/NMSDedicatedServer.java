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
}
