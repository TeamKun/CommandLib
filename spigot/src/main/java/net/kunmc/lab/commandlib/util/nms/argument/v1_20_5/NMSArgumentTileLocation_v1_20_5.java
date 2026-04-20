package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTileLocation;
import net.kunmc.lab.commandlib.util.nms.world.NMSIBlockData;

public class NMSArgumentTileLocation_v1_20_5 extends NMSArgumentTileLocation {
    public NMSArgumentTileLocation_v1_20_5(Object handle) {
        super(handle, "commands.arguments.blocks.BlockInput");
    }

    public NMSIBlockData getBlockData() {
        return NMSIBlockData.create(invokeMethod("getState"));
    }
}
