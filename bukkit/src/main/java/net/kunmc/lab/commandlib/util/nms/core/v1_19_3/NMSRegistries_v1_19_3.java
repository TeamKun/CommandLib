package net.kunmc.lab.commandlib.util.nms.core.v1_19_3;

import net.kunmc.lab.commandlib.util.nms.core.NMSRegistries;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;

public class NMSRegistries_v1_19_3 extends NMSRegistries {
    public NMSRegistries_v1_19_3() {
        super(null, "core.registries.Registries");
    }

    @Override
    public NMSResourceKey enchantment() {
        return NMSResourceKey.create(getValue(Object.class, "q", "ENCHANTMENT"));
    }

    @Override
    public NMSResourceKey mobEffect() {
        return NMSResourceKey.create(getValue(Object.class, "N", "MOB_EFFECT"));
    }
}
