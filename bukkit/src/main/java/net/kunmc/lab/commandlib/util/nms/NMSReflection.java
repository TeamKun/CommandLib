package net.kunmc.lab.commandlib.util.nms;

import net.kunmc.lab.commandlib.util.nms.exception.NMSClassNotFoundException;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class NMSReflection {
    private static final String VERSION;
    private static final String MINECRAFT_PACKAGE_PREFIX = "net.minecraft";
    private static final String NMS_PACKAGE_PREFIX;
    private static final String CRAFT_BUKKIT_PACKAGE_PREFIX;
    private static final Map<String, Optional<Class<?>>> CACHE = new ConcurrentHashMap<>();
    private static final ClassLoader CLASS_LOADER = Bukkit.getServer()
                                                          .getClass()
                                                          .getClassLoader();

    static {
        VERSION = Bukkit.getServer()
                        .getClass()
                        .getPackage()
                        .getName()
                        .split("\\.")[3];
        NMS_PACKAGE_PREFIX = "net.minecraft.server." + VERSION;
        CRAFT_BUKKIT_PACKAGE_PREFIX = "org.bukkit.craftbukkit." + VERSION;
    }

    public static Class<?> findMinecraftClass(String className, String... classNames) {
        return Stream.concat(Stream.of(className), Stream.of(classNames))
                     .map(x -> {
                         Class<?> nmsClass = findClass(NMS_PACKAGE_PREFIX + "." + x).orElse(null);
                         if (nmsClass != null) {
                             return nmsClass;
                         }
                         return findClass(MINECRAFT_PACKAGE_PREFIX + "." + x).orElse(null);
                     })
                     .filter(Objects::nonNull)
                     .findFirst()
                     .orElseThrow(() -> new NMSClassNotFoundException(className, classNames));
    }

    public static Class<?> findCraftBukkitClass(String className) {
        return findClass(CRAFT_BUKKIT_PACKAGE_PREFIX + "." + className).orElseThrow(() -> new NMSClassNotFoundException(
                className));
    }

    private static Optional<Class<?>> findClass(String className) {
        return CACHE.computeIfAbsent(className, k -> {
            try {
                return Optional.of(CLASS_LOADER.loadClass(k));
            } catch (ClassNotFoundException e) {
                return Optional.empty();
            }
        });
    }
}
