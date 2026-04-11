package net.kunmc.lab.commandlib.util.nms;

import net.kunmc.lab.commandlib.util.nms.exception.NMSClassNotFoundException;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NMSClassResolverTest {
    @Test
    void findMinecraftClass_prefers_nms_package_before_fallback_package() {
        NMSClassResolver resolver = new NMSClassResolver(
                new MapBackedClassLoader(Map.of(
                        "net.minecraft.server.v1_16_R3.DedicatedServer", String.class,
                        "net.minecraft.DedicatedServer", Integer.class
                )),
                "net.minecraft.server.v1_16_R3",
                "org.bukkit.craftbukkit.v1_16_R3"
        );

        assertThat(resolver.findMinecraftClass("DedicatedServer")).isEqualTo(String.class);
    }

    @Test
    void findMinecraftClass_falls_back_to_modern_net_minecraft_package() {
        NMSClassResolver resolver = new NMSClassResolver(
                new MapBackedClassLoader(Map.of(
                        "net.minecraft.commands.CommandBuildContext", Integer.class
                )),
                "net.minecraft.server",
                "org.bukkit.craftbukkit"
        );

        assertThat(resolver.findMinecraftClass("commands.CommandBuildContext")).isEqualTo(Integer.class);
    }

    @Test
    void findCraftBukkitClass_uses_configured_prefix() {
        NMSClassResolver resolver = new NMSClassResolver(
                new MapBackedClassLoader(Map.of(
                        "org.bukkit.craftbukkit.v1_16_R3.CraftServer", Long.class
                )),
                "net.minecraft.server.v1_16_R3",
                "org.bukkit.craftbukkit.v1_16_R3"
        );

        assertThat(resolver.findCraftBukkitClass("CraftServer")).isEqualTo(Long.class);
    }

    @Test
    void findCraftBukkitClass_throws_when_class_does_not_exist() {
        NMSClassResolver resolver = new NMSClassResolver(
                new MapBackedClassLoader(Map.of()),
                "net.minecraft.server.v1_16_R3",
                "org.bukkit.craftbukkit.v1_16_R3"
        );

        assertThatThrownBy(() -> resolver.findCraftBukkitClass("Missing"))
                .isInstanceOf(NMSClassNotFoundException.class);
    }

    @Test
    void loads_real_legacy_nms_and_craftbukkit_classes_from_patched_server_jar_when_available() throws Exception {
        Path jarPath = resolveJarPath("COMMANDLIB_NMS_TEST_JAR_1_16_5",
                                      "bukkit-integration-test/fixtures/test-plugin-1.16.5/server/cache/patched_1.16.5.jar");
        if (!Files.exists(jarPath)) {
            return;
        }

        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jarPath.toUri().toURL()}, null)) {
            NMSClassResolver resolver = new NMSClassResolver(classLoader,
                                                             "net.minecraft.server.v1_16_R3",
                                                             "org.bukkit.craftbukkit.v1_16_R3");

            assertThat(resolver.findMinecraftClass("DedicatedServer").getName())
                    .isEqualTo("net.minecraft.server.v1_16_R3.DedicatedServer");
            assertThat(resolver.findCraftBukkitClass("CraftServer").getName())
                    .isEqualTo("org.bukkit.craftbukkit.v1_16_R3.CraftServer");
        }
    }

    private static Path resolveJarPath(String envName, String relativeFallback) {
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.isBlank()) {
            return Paths.get(envValue);
        }
        Path cwd = Paths.get("").toAbsolutePath();
        Path direct = cwd.resolve(relativeFallback).normalize();
        if (Files.exists(direct)) {
            return direct;
        }
        Path parent = cwd.getParent();
        if (parent != null) {
            return parent.resolve(relativeFallback).normalize();
        }
        return direct;
    }

    private static final class MapBackedClassLoader extends ClassLoader {
        private final Map<String, Class<?>> classes;

        private MapBackedClassLoader(Map<String, Class<?>> classes) {
            super(null);
            this.classes = classes;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            Class<?> clazz = classes.get(name);
            if (clazz != null) {
                return clazz;
            }
            throw new ClassNotFoundException(name);
        }
    }
}
