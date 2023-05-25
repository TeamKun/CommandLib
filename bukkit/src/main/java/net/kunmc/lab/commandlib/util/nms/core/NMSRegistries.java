package net.kunmc.lab.commandlib.util.nms.core;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;

public class NMSRegistries extends MinecraftClass {
    public NMSRegistries() {
        super(null, "core.registries.Registries");
    }

    public NMSResourceKey enchantment() {
        return new NMSResourceKey(getValue(Object.class, "q", "ENCHANTMENT"));
    }

    public NMSResourceKey mobEffect() {
        return new NMSResourceKey(getValue(Object.class, "N", "MOB_EFFECT"));
    }
}
