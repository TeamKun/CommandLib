package net.kunmc.lab.commandlib.util.nms.command;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.command.v1_19_0.NMSCommandBuildContext_v1_19_0;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;

/**
 * for after 1.19.0
 */
public abstract class NMSCommandBuildContext extends MinecraftClass {
    public static NMSCommandBuildContext create(Object handle) {
        return ReflectionUtil.getConstructor(NMSClassRegistry.findClass(NMSCommandBuildContext.class), Object.class)
                             .newInstance(handle);
    }

    public NMSCommandBuildContext(Object handle, String className) {
        super(handle, className);
    }

    static {
        NMSClassRegistry.register(NMSCommandBuildContext.class,
                                  NMSCommandBuildContext_v1_19_0.class,
                                  "1.19.0",
                                  "9.9.9");
    }
}
