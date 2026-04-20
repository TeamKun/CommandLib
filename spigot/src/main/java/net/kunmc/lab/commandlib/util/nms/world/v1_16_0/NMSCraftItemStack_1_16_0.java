package net.kunmc.lab.commandlib.util.nms.world.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSCraftItemStack;
import net.kunmc.lab.commandlib.util.nms.world.NMSItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSCraftItemStack_1_16_0 extends NMSCraftItemStack {
    public NMSCraftItemStack_1_16_0() {
        super(null, "inventory.CraftItemStack");
    }

    @Override
    public ItemStack asCraftMirror(NMSItemStack nms) {
        return ((ItemStack) invokeStaticMethod("asCraftMirror", nms.getHandle()));
    }
}
