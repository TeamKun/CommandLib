package net.kunmc.lab.commandlib.util.nms.command.v1_20_5;

import com.mojang.brigadier.CommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;

public class NMSCommandDispatcher_v1_20_5 extends NMSCommandDispatcher {
    public NMSCommandDispatcher_v1_20_5(Object handle) {
        super(handle, "commands.Commands");
    }

    @Override
    public CommandDispatcher<?> getBrigadier() {
        return ((CommandDispatcher<?>) invokeMethod("getDispatcher"));
    }
}
