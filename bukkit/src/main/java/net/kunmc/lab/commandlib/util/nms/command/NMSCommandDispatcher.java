package net.kunmc.lab.commandlib.util.nms.command;

import com.mojang.brigadier.CommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSCommandDispatcher extends MinecraftClass {
    public NMSCommandDispatcher(Object handle) {
        super(handle, "CommandDispatcher", "commands.CommandDispatcher", "commands.Commands");
    }

    public CommandDispatcher<?> getBrigadier() {
        return ((CommandDispatcher<?>) invokeMethod("a", "getDispatcher"));
    }
}
