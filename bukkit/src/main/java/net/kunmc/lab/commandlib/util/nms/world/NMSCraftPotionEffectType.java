package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.bukkit.VersionUtil;
import net.kunmc.lab.commandlib.util.nms.CraftBukkitClass;
import org.bukkit.potion.PotionEffectType;

public class NMSCraftPotionEffectType extends CraftBukkitClass {
    public NMSCraftPotionEffectType() {
        super(null, "potion.CraftPotionEffectType");
    }

    public NMSCraftPotionEffectType(Object handle) {
        super(handle, "potion.CraftPotionEffectType");
    }

    public PotionEffectType createInstance(NMSMobEffectList nms) {
        if (VersionUtil.is1_20_x()) {
            return ((PotionEffectType) invokeMethod("minecraftToBukkit",
                                                    new Class[]{nms.getFoundClass()},
                                                    nms.getHandle()));
        }

        return ((PotionEffectType) newInstance(new Class[]{nms.getFoundClass()}, new Object[]{nms.getHandle()}));
    }
}
