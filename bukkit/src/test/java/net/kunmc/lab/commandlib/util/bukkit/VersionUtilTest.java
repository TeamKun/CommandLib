package net.kunmc.lab.commandlib.util.bukkit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VersionUtilTest {
    @Nested
    class CompareMinecraftVersion {
        @Test
        void returns_zero_with_same_values() {
            int res = VersionUtil.compareMinecraftVersion("1.16.5", "1.16.5");
            Assertions.assertThat(res)
                      .isEqualTo(0);
        }

        @Test
        void returns_negative_when_x_is_lower() {
            int res = VersionUtil.compareMinecraftVersion("1.16.5", "1.20.4");
            Assertions.assertThat(res)
                      .isLessThan(0);
        }

        @Test
        void returns_positive_when_x_is_greater() {
            int res = VersionUtil.compareMinecraftVersion("1.20.4", "1.16.5");
            Assertions.assertThat(res)
                      .isGreaterThan(0);
        }
    }
}
