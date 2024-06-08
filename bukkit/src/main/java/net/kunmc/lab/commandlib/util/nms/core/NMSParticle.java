package net.kunmc.lab.commandlib.util.nms.core;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.core.v1_16_0.NMSParticle_v1_16_0;
import net.kunmc.lab.commandlib.util.nms.core.v1_17_0.NMSParticle_v1_17_0;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;

public abstract class NMSParticle extends MinecraftClass {
    public static NMSParticle create(Object handle) {
        return ReflectionUtil.getConstructor(NMSClassRegistry.findClass(NMSParticle.class), Object.class)
                             .newInstance(handle);
    }

    public NMSParticle(Object handle, String className, String... classNames) {
        super(handle, className, classNames);
    }

    static {
        NMSClassRegistry.register(NMSParticle.class, NMSParticle_v1_16_0.class, "1.16.0", "1.16.5");
        NMSClassRegistry.register(NMSParticle.class, NMSParticle_v1_17_0.class, "1.17.0", "1.20.4");
    }
}
