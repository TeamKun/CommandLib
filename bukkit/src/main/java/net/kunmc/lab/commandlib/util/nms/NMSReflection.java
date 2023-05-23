package net.kunmc.lab.commandlib.util.nms;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class NMSReflection {
    private static final String version;
    private static final String minecraftPackagePrefix = "net.minecraft";
    private static final String nmsPackagePrefix;
    private static final String craftBukkitPackagePrefix;
    private static final Map<String, Optional<Class<?>>> cache = new HashMap<>();
    private static final ClassLoader classLoader = Bukkit.getServer()
                                                         .getClass()
                                                         .getClassLoader();

    static {
        version = Bukkit.getServer()
                        .getClass()
                        .getPackage()
                        .getName()
                        .split("\\.")[3];
        nmsPackagePrefix = "net.minecraft.server." + version;
        craftBukkitPackagePrefix = "org.bukkit.craftbukkit." + version;
    }

    public static Optional<Class<?>> findMinecraftClass(String className, String... classNames) {
        return Stream.concat(Stream.of(className), Stream.of(classNames))
                     .map(x -> {
                         Class<?> nmsClass = findClass(nmsPackagePrefix + "." + x).orElse(null);
                         if (nmsClass != null) {
                             return nmsClass;
                         }
                         return findClass(minecraftPackagePrefix + "." + x).orElse(null);
                     })
                     .filter(Objects::nonNull)
                     .findFirst();
    }

    public static Optional<Class<?>> findCraftBukkitClass(String className) {
        return findClass(craftBukkitPackagePrefix + "." + className);
    }

    private static Optional<Class<?>> findClass(String className) {
        return cache.computeIfAbsent(className, k -> {
            try {
                return Optional.of(classLoader.loadClass(k));
            } catch (ClassNotFoundException e) {
                return Optional.empty();
            }
        });
    }
}
