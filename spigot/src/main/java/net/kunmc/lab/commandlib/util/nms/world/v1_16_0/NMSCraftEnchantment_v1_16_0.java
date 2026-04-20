package net.kunmc.lab.commandlib.util.nms.world.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSCraftEnchantment;
import net.kunmc.lab.commandlib.util.nms.world.NMSEnchantment;
import org.bukkit.enchantments.Enchantment;

public class NMSCraftEnchantment_v1_16_0 extends NMSCraftEnchantment {
    public NMSCraftEnchantment_v1_16_0() {
        super(null, "enchantments.CraftEnchantment");
    }

    @Override
    public Enchantment createInstance(NMSEnchantment nms) {
        return ((Enchantment) newInstance(new Class[]{nms.getFoundClass()}, new Object[]{nms.getHandle()}));
    }
}
