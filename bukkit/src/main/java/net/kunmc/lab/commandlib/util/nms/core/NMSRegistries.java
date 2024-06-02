package net.kunmc.lab.commandlib.util.nms.core;

import net.kunmc.lab.commandlib.util.bukkit.VersionUtil;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;

public class NMSRegistries extends MinecraftClass {
    public NMSRegistries() {
        super(null, "core.registries.Registries");
    }

    public NMSResourceKey enchantment() {
        if (VersionUtil.is1_20_x()) {
            return new NMSResourceKey(getValue(Object.class, "t", "ENCHANTMENT"));
        }

        return new NMSResourceKey(getValue(Object.class, "q", "ENCHANTMENT"));
    }

    public NMSResourceKey mobEffect() {
        if (VersionUtil.is1_20_x()) {
            return new NMSResourceKey(getValue(Object.class, "Q", "MOB_EFFECT"));
        }

        return new NMSResourceKey(getValue(Object.class, "N", "MOB_EFFECT"));
    }
}
