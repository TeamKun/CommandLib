package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.bukkit.VersionUtil;
import net.kunmc.lab.commandlib.util.nms.CraftBukkitClass;
import org.bukkit.enchantments.Enchantment;

public class NMSCraftEnchantment extends CraftBukkitClass {
    public NMSCraftEnchantment() {
        super(null, "enchantments.CraftEnchantment");
    }

    public Enchantment createInstance(NMSEnchantment nms) {
        if (VersionUtil.is1_20_x()) {
            return ((Enchantment) invokeMethod("minecraftToBukkit", new Class[]{nms.getFoundClass()}, nms.getHandle()));
        }

        return ((Enchantment) newInstance(new Class[]{nms.getFoundClass()}, new Object[]{nms.getHandle()}));
    }
}
