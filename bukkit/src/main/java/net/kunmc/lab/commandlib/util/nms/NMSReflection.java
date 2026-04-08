package net.kunmc.lab.commandlib.util.nms;

import net.kunmc.lab.commandlib.util.nms.exception.NMSClassNotFoundException;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class NMSReflection {
    private static final String MINECRAFT_PACKAGE_PREFIX = "net.minecraft";
    private static final String NMS_PACKAGE_PREFIX;
    private static final String CRAFT_BUKKIT_PACKAGE_PREFIX;
    private static final Map<String, Class<?>> CACHE = new ConcurrentHashMap<>();
    private static final ClassLoader CLASS_LOADER;

    static {
        org.bukkit.Server server = Bukkit.getServer();
        CLASS_LOADER = server != null ? server.getClass()
                                              .getClassLoader() : Thread.currentThread()
                                                                        .getContextClassLoader();

        String version = null;
        if (server != null) {
            try {
                version = server.getClass()
                                .getPackage()
                                .getName()
                                .split("\\.")[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                // Since 1.20.5, the version is no longer included in the package name
            }
        }

        if (version == null) {
            NMS_PACKAGE_PREFIX = "net.minecraft.server";
            CRAFT_BUKKIT_PACKAGE_PREFIX = "org.bukkit.craftbukkit";
        } else {
            NMS_PACKAGE_PREFIX = "net.minecraft.server." + version;
            CRAFT_BUKKIT_PACKAGE_PREFIX = "org.bukkit.craftbukkit." + version;
        }
    }

    @NotNull
    public static Class<?> findMinecraftClass(String className, String... classNames) {
        return Stream.concat(Stream.of(className), Stream.of(classNames))
                     .map(x -> {
                         Class<?> nmsClass = findClass(NMS_PACKAGE_PREFIX + "." + x);
                         if (nmsClass != null) {
                             return nmsClass;
                         }
                         return findClass(MINECRAFT_PACKAGE_PREFIX + "." + x);
                     })
                     .filter(Objects::nonNull)
                     .findFirst()
                     .orElseThrow(() -> new NMSClassNotFoundException(className, classNames));
    }

    @NotNull
    public static Class<?> findCraftBukkitClass(String className) {
        Class<?> clazz = findClass(CRAFT_BUKKIT_PACKAGE_PREFIX + "." + className);
        if (clazz == null) {
            throw new NMSClassNotFoundException(className);
        }
        return clazz;
    }

    @Nullable
    public static Class<?> findClass(String className) {
        return CACHE.computeIfAbsent(className, k -> {
            try {
                return CLASS_LOADER.loadClass(k);
            } catch (ClassNotFoundException e) {
                return null;
            }
        });
    }
}
