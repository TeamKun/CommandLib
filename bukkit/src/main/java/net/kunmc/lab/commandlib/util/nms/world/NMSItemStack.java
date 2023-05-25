package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSItemStack extends MinecraftClass {
    public NMSItemStack(Object handle) {
        super(handle, "ItemStack", "world.item.ItemStack", "world.item.Item");
    }
}
