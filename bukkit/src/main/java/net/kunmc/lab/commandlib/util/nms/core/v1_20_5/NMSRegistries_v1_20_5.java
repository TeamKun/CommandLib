package net.kunmc.lab.commandlib.util.nms.core.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.core.NMSRegistries;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;

public class NMSRegistries_v1_20_5 extends NMSRegistries {
    public NMSRegistries_v1_20_5() {
        super(null, "core.registries.Registries");
    }

    @Override
    public NMSResourceKey enchantment() {
        return NMSResourceKey.create(getValue(Object.class, "ENCHANTMENT"));
    }

    @Override
    public NMSResourceKey mobEffect() {
        return NMSResourceKey.create(getValue(Object.class, "MOB_EFFECT"));
    }
}
