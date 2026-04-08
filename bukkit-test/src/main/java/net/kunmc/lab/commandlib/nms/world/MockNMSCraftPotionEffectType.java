package net.kunmc.lab.commandlib.nms.world;

import net.kunmc.lab.commandlib.util.nms.world.NMSCraftPotionEffectType;
import net.kunmc.lab.commandlib.util.nms.world.NMSMobEffectList;
import org.bukkit.potion.PotionEffectType;

public class MockNMSCraftPotionEffectType extends NMSCraftPotionEffectType {
    public MockNMSCraftPotionEffectType() {
        super(null, "Mock");
    }

    @Override
    public PotionEffectType createInstance(NMSMobEffectList nms) {
        String name = ((MockNMSMobEffectList) nms).getMockName();
        return PotionEffectType.getByName(name.toUpperCase());
    }
}
