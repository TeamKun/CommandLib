package net.kunmc.lab.commandlib.util.nms;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NMSReflection {
    private static final NMSClassResolver DEFAULT_RESOLVER = NMSClassResolver.createDefault();

    @NotNull
    public static Class<?> findMinecraftClass(String className, String... classNames) {
        return DEFAULT_RESOLVER.findMinecraftClass(className, classNames);
    }

    @NotNull
    public static Class<?> findCraftBukkitClass(String className) {
        return DEFAULT_RESOLVER.findCraftBukkitClass(className);
    }

    @Nullable
    public static Class<?> findClass(String className) {
        return DEFAULT_RESOLVER.findClass(className);
    }
}
