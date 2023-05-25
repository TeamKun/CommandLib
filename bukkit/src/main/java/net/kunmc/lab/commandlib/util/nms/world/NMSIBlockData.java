package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSIBlockData extends MinecraftClass {
    public NMSIBlockData(Object handle) {
        super(handle, "IBlockData", "world.level.block.state.IBlockData", "world.level.block.state.BlockState");
    }
}
