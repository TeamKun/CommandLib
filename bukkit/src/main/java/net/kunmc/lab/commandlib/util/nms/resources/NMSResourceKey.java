package net.kunmc.lab.commandlib.util.nms.resources;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.resources.v1_16_0.NMSResourceKey_v1_16_0;
import net.kunmc.lab.commandlib.util.nms.resources.v1_17_0.NMSResourceKey_v1_17_0;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;

public abstract class NMSResourceKey extends MinecraftClass {
    public static NMSResourceKey create(Object handle) {
        return ReflectionUtil.getConstructor(NMSClassRegistry.findClass(NMSResourceKey.class), Object.class)
                             .newInstance(handle);
    }

    public NMSResourceKey(Object handle, String className) {
        super(handle, className);
    }

    static {
        NMSClassRegistry.register(NMSResourceKey.class, NMSResourceKey_v1_16_0.class, "1.16.0", "1.16.5");
        NMSClassRegistry.register(NMSResourceKey.class, NMSResourceKey_v1_17_0.class, "1.17.0", "1.20.4");
    }
}
