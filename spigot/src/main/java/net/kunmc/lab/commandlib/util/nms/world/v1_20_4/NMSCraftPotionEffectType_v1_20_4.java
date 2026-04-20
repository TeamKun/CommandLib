package net.kunmc.lab.commandlib.util.nms.world.v1_20_4;

import net.kunmc.lab.commandlib.util.nms.world.NMSCraftPotionEffectType;
import net.kunmc.lab.commandlib.util.nms.world.NMSMobEffectList;
import org.bukkit.potion.PotionEffectType;

public class NMSCraftPotionEffectType_v1_20_4 extends NMSCraftPotionEffectType {
    public NMSCraftPotionEffectType_v1_20_4() {
        super(null, "potion.CraftPotionEffectType");
    }

    public NMSCraftPotionEffectType_v1_20_4(Object handle) {
        super(handle, "potion.CraftPotionEffectType");
    }

    @Override
    public PotionEffectType createInstance(NMSMobEffectList nms) {
        return ((PotionEffectType) invokeStaticMethod("minecraftToBukkit",
                                                      new Class[]{nms.getFoundClass()},
                                                      nms.getHandle()));
    }
}
