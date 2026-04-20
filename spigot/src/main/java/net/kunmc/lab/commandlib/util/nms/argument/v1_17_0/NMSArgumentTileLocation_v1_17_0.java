package net.kunmc.lab.commandlib.util.nms.argument.v1_17_0;

import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTileLocation;
import net.kunmc.lab.commandlib.util.nms.world.NMSIBlockData;

public class NMSArgumentTileLocation_v1_17_0 extends NMSArgumentTileLocation {
    public NMSArgumentTileLocation_v1_17_0(Object handle) {
        super(handle, "commands.arguments.blocks.ArgumentTileLocation");
    }

    public NMSIBlockData getBlockData() {
        return NMSIBlockData.create(invokeMethod("a"));
    }
}
