package net.kunmc.lab.commandlib.util.nms;

public abstract class MinecraftClass extends NMSClass {
    public MinecraftClass(Object handle, String className, String... classNames) {
        super(handle, NMSReflection.findMinecraftClass(className, classNames));
    }
}
