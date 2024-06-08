package net.kunmc.lab.commandlib.util.nms;

import net.kunmc.lab.commandlib.util.bukkit.MinecraftVersion;
import net.kunmc.lab.commandlib.util.nms.exception.UnregisteredNMSClassException;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NMSClassRegistry {
    private static final Logger logger = LoggerFactory.getLogger(NMSClassRegistry.class);
    private static final Map<Class<? extends NMSClass>, Deque<RegisteredClass>> map = new ConcurrentHashMap<>();

    public static <T extends NMSClass> void register(Class<T> lookUpClass,
                                                     Class<? extends T> targetClass,
                                                     String lowerVersion,
                                                     String upperVersion) {
        Objects.requireNonNull(lookUpClass);
        Objects.requireNonNull(targetClass);

        Deque<RegisteredClass> deque = map.getOrDefault(lookUpClass, new ConcurrentLinkedDeque<>());
        deque.addFirst(new RegisteredClass(targetClass,
                                           new MinecraftVersion(lowerVersion),
                                           new MinecraftVersion(upperVersion)));
        map.put(lookUpClass, deque);
    }

    public static <T extends Class<? extends NMSClass>> T findClass(T clazz) {
        Deque<RegisteredClass> deque = map.get(clazz);
        if (deque == null) {
            throw new UnregisteredNMSClassException(clazz + " is unregistered.");
        }

        for (RegisteredClass registeredClass : deque) {
            boolean isWithin = new MinecraftVersion(Bukkit.getMinecraftVersion()).isWithin(registeredClass.lowerVersion,
                                                                                           registeredClass.upperVersion);
            if (isWithin) {
                return ((T) registeredClass.clazz);
            }
        }

        throw new UnregisteredNMSClassException(clazz + " is unregistered.");
    }

    private static class RegisteredClass {
        public final Class<? extends NMSClass> clazz;
        public final MinecraftVersion lowerVersion;
        public final MinecraftVersion upperVersion;

        private RegisteredClass(Class<? extends NMSClass> clazz,
                                MinecraftVersion lowerVersion,
                                MinecraftVersion upperVersion) {
            this.clazz = clazz;
            this.lowerVersion = lowerVersion;
            this.upperVersion = upperVersion;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RegisteredClass that = (RegisteredClass) o;

            if (!Objects.equals(lowerVersion, that.lowerVersion)) {
                return false;
            }
            if (!Objects.equals(upperVersion, that.upperVersion)) {
                return false;
            }
            return Objects.equals(clazz, that.clazz);
        }

        @Override
        public int hashCode() {
            int result = lowerVersion != null ? lowerVersion.hashCode() : 0;
            result = 31 * result + (upperVersion != null ? upperVersion.hashCode() : 0);
            result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
            return result;
        }
    }
}
