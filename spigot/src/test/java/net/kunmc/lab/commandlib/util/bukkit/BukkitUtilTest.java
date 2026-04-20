package net.kunmc.lab.commandlib.util.bukkit;

import org.assertj.core.api.Assertions;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class BukkitUtilTest {
    @Test
    void getMinecraftVersion() {
        Map<String, String> bukkitVersionToExpected = new HashMap<>();
        bukkitVersionToExpected.put("1.20-R0.1-SNAPSHOT", "1.20");
        bukkitVersionToExpected.put("1.20.1-R0.1-SNAPSHOT", "1.20.1");

        try (MockedStatic<Bukkit> mockedStatic = Mockito.mockStatic(Bukkit.class)) {
            for (Map.Entry<String, String> entry : bukkitVersionToExpected.entrySet()) {
                String bukkitVersion = entry.getKey();
                String expected = entry.getValue();

                mockedStatic.when(Bukkit::getBukkitVersion)
                            .thenReturn(bukkitVersion);

                String version = BukkitUtil.getMinecraftVersion();
                Assertions.assertThat(version)
                          .isEqualTo(expected);
            }
        }
    }
}
