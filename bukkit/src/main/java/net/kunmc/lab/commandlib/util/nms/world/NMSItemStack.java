package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.world.v1_16_0.NMSItemStack_v1_16_0;
import net.kunmc.lab.commandlib.util.nms.world.v1_17_0.NMSItemStack_v1_17_0;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;

public abstract class NMSItemStack extends MinecraftClass {
    public static NMSItemStack create(Object handle) {
        return ReflectionUtil.getConstructor(NMSClassRegistry.findClass(NMSItemStack.class), Object.class)
                             .newInstance(handle);
    }

    public NMSItemStack(Object handle, String className, String... classNames) {
        super(handle, className, classNames);
    }

    static {
        NMSClassRegistry.register(NMSItemStack.class, NMSItemStack_v1_16_0.class, "1.16.0", "1.16.5");
        NMSClassRegistry.register(NMSItemStack.class, NMSItemStack_v1_17_0.class, "1.17.0", "1.20.4");
    }
}
