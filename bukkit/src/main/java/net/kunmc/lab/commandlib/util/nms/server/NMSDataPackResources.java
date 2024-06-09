package net.kunmc.lab.commandlib.util.nms.server;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.server.v1_16_0.NMSDataPackResources_v1_16_0;
import net.kunmc.lab.commandlib.util.nms.server.v1_17_0.NMSDataPackResources_v1_17_0;
import net.kunmc.lab.commandlib.util.nms.server.v1_19_0.NMSDataPackResources_v1_19_0;
import net.kunmc.lab.commandlib.util.nms.server.v1_20_4.NMSDataPackResources_v1_20_4;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;

public abstract class NMSDataPackResources extends MinecraftClass {
    public static NMSDataPackResources create(Object handle) {
        return ReflectionUtil.getConstructor(NMSClassRegistry.findClass(NMSDataPackResources.class), Object.class)
                             .newInstance(handle);
    }

    public NMSDataPackResources(Object handle, String className, String... classNames) {
        super(handle, className, classNames);
    }

    /**
     * for after 1.19.0
     */
    public abstract NMSCommandBuildContext getCommandBuildContext();

    static {
        NMSClassRegistry.register(NMSDataPackResources.class, NMSDataPackResources_v1_16_0.class, "1.16.0", "1.16.5");
        NMSClassRegistry.register(NMSDataPackResources.class, NMSDataPackResources_v1_17_0.class, "1.17.0", "1.18.2");
        NMSClassRegistry.register(NMSDataPackResources.class, NMSDataPackResources_v1_19_0.class, "1.19.0", "1.19.4");
        NMSClassRegistry.register(NMSDataPackResources.class, NMSDataPackResources_v1_20_4.class, "1.20.4", "1.20.4");
    }
}
