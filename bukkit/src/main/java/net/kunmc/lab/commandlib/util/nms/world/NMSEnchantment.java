package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.world.v1_16_0.NMSEnchantment_v1_16_0;
import net.kunmc.lab.commandlib.util.nms.world.v1_17_0.NMSEnchantment_v1_17_0;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;

public abstract class NMSEnchantment extends MinecraftClass {
    public static NMSEnchantment create(Object handle) {
        return ReflectionUtil.getConstructor(NMSClassRegistry.findClass(NMSEnchantment.class), Object.class)
                             .newInstance(handle);
    }

    public NMSEnchantment(Object handle, String className) {
        super(handle, className);
    }

    static {
        NMSClassRegistry.register(NMSEnchantment.class, NMSEnchantment_v1_16_0.class, "1.16.0", "1.16.5");
        NMSClassRegistry.register(NMSEnchantment.class, NMSEnchantment_v1_17_0.class, "1.17.0", "1.20.4");
    }
}
