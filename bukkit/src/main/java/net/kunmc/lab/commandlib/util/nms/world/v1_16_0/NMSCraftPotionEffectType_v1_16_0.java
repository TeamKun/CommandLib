package net.kunmc.lab.commandlib.util.nms.world.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSCraftPotionEffectType;
import net.kunmc.lab.commandlib.util.nms.world.NMSMobEffectList;
import org.bukkit.potion.PotionEffectType;

public class NMSCraftPotionEffectType_v1_16_0 extends NMSCraftPotionEffectType {
    public NMSCraftPotionEffectType_v1_16_0() {
        this(null);
    }

    public NMSCraftPotionEffectType_v1_16_0(Object handle) {
        super(handle, "potion.CraftPotionEffectType");
    }

    @Override
    public PotionEffectType createInstance(NMSMobEffectList nms) {
        return ((PotionEffectType) newInstance(new Class[]{nms.getFoundClass()}, new Object[]{nms.getHandle()}));
    }
}
