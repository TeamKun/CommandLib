package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.CraftBukkitClass;
import org.bukkit.inventory.ItemStack;

public class NMSCraftItemStack extends CraftBukkitClass {
    public NMSCraftItemStack() {
        super(null, "inventory.CraftItemStack");
    }

    public ItemStack asCraftMirror(NMSItemStack nms) {
        return ((ItemStack) invokeMethod("asCraftMirror", nms.getHandle()));
    }
}
