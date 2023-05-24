package net.kunmc.lab.commandlib.util.nms.argument;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.world.NMSIBlockData;

public class NMSArgumentTileLocation extends MinecraftClass {
    public NMSArgumentTileLocation(Object handle) {
        super(handle,
              "ArgumentTileLocation",
              "commands.arguments.blocks.ArgumentTileLocation",
              "commands.arguments.blocks.BlockInput");
    }

    public NMSIBlockData getBlockData() {
        return new NMSIBlockData(invokeMethod("a", "getState"));
    }
}
