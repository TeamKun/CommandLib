package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.CraftBukkitClass;
import org.bukkit.block.data.BlockData;

public class NMSCraftBlockData extends CraftBukkitClass {
    public NMSCraftBlockData() {
        super(null, "block.data.CraftBlockData");
    }

    public BlockData createData(NMSIBlockData nms) {
        return ((BlockData) invokeMethod("createData", nms.getHandle()));
    }
}
