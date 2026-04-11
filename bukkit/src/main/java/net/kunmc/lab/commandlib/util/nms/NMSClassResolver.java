package net.kunmc.lab.commandlib.util.nms;

import net.kunmc.lab.commandlib.util.nms.exception.NMSClassNotFoundException;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class NMSClassResolver {
    private static final String MINECRAFT_PACKAGE_PREFIX = "net.minecraft";

    private final String nmsPackagePrefix;
    private final String craftBukkitPackagePrefix;
    private final ClassLoader classLoader;
    private final Map<String, Class<?>> cache = new ConcurrentHashMap<>();

    public NMSClassResolver(@NotNull ClassLoader classLoader,
                            @NotNull String nmsPackagePrefix,
                            @NotNull String craftBukkitPackagePrefix) {
        this.classLoader = Objects.requireNonNull(classLoader);
        this.nmsPackagePrefix = Objects.requireNonNull(nmsPackagePrefix);
        this.craftBukkitPackagePrefix = Objects.requireNonNull(craftBukkitPackagePrefix);
    }

    @NotNull
    public static NMSClassResolver createDefault() {
        return fromServer(BukkitAccess.getServer());
    }

    @NotNull
    public static NMSClassResolver fromServer(@Nullable Server server) {
        ClassLoader classLoader = server != null ? server.getClass().getClassLoader()
                                                 : Thread.currentThread().getContextClassLoader();

        String version = null;
        if (server != null) {
            try {
                version = server.getClass().getPackage().getName().split("\\.")[3];
            } catch (ArrayIndexOutOfBoundsException ignored) {
                // Since 1.20.5, the version is no longer included in the package name.
            }
        }

        if (version == null) {
            return new NMSClassResolver(classLoader, "net.minecraft.server", "org.bukkit.craftbukkit");
        }
        return new NMSClassResolver(classLoader,
                                    "net.minecraft.server." + version,
                                    "org.bukkit.craftbukkit." + version);
    }

    @NotNull
    public Class<?> findMinecraftClass(String className, String... classNames) {
        return Stream.concat(Stream.of(className), Stream.of(classNames))
                     .map(x -> {
                         Class<?> nmsClass = findClass(nmsPackagePrefix + "." + x);
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
    public Class<?> findCraftBukkitClass(String className) {
        Class<?> clazz = findClass(craftBukkitPackagePrefix + "." + className);
        if (clazz == null) {
            throw new NMSClassNotFoundException(className);
        }
        return clazz;
    }

    @Nullable
    public Class<?> findClass(String className) {
        return cache.computeIfAbsent(className, k -> {
            try {
                return classLoader.loadClass(k);
            } catch (ClassNotFoundException e) {
                return null;
            }
        });
    }

    private static final class BukkitAccess {
        private static org.bukkit.Server getServer() {
            return org.bukkit.Bukkit.getServer();
        }
    }
}
