package net.kunmc.lab.commandlib.util.nms.argument;

import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.argument.v1_16_0.NMSArgumentProfile_v1_16_0;
import net.kunmc.lab.commandlib.util.nms.argument.v1_17_0.NMSArgumentProfile_v1_17_0;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;

public abstract class NMSArgumentProfile extends NMSArgument<Object> {
    public static NMSArgumentProfile create() {
        return ReflectionUtil.getConstructor(NMSClassRegistry.findClass(NMSArgumentProfile.class))
                             .newInstance();
    }

    public NMSArgumentProfile(Object handle, String className, String... classNames) {
        super(handle, className, classNames);
    }

    static {
        NMSClassRegistry.register(NMSArgumentProfile.class, NMSArgumentProfile_v1_16_0.class, "1.16.0", "1.16.5");
        NMSClassRegistry.register(NMSArgumentProfile.class, NMSArgumentProfile_v1_17_0.class, "1.17.0", "1.20.4");
    }
}
