package net.kunmc.lab.commandlib.util.nms.command.v1_17_0;

import com.mojang.brigadier.CommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;

public class NMSCommandDispatcher_v1_17_0 extends NMSCommandDispatcher {
    public NMSCommandDispatcher_v1_17_0(Object handle) {
        super(handle, "commands.CommandDispatcher");
    }

    @Override
    public CommandDispatcher<?> getBrigadier() {
        return ((CommandDispatcher<?>) invokeMethod("a", "getDispatcher"));
    }
}
