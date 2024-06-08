package net.kunmc.lab.commandlib.util.nms.world.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSCraftBlockData;
import net.kunmc.lab.commandlib.util.nms.world.NMSIBlockData;
import org.bukkit.block.data.BlockData;

public class NMSCraftBlockData_v1_16_0 extends NMSCraftBlockData {
    public NMSCraftBlockData_v1_16_0() {
        super(null, "block.data.CraftBlockData");
    }

    @Override
    public BlockData createData(NMSIBlockData nms) {
        return ((BlockData) invokeStaticMethod("createData", nms.getHandle()));
    }
}
