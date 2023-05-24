package net.kunmc.lab.commandlib.util.nms.command;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

// 1.16.5には存在しない
public class NMSCommandBuildContext extends MinecraftClass {
    public NMSCommandBuildContext(Object handle) {
        super(handle, "commands.CommandBuildContext");
    }
}
