package net.kunmc.lab.commandlib.util.bukkit;

import org.bukkit.Bukkit;

import java.util.regex.Pattern;

public class VersionUtil {
    // パッチバージョンが0の時、1.20のような形式となる
    private static final Pattern MINECRAFT_WITHOUT_PATCH_VERSION_PATTERN = Pattern.compile("\\d+.\\d+");
    private static final Pattern MINECRAFT_VERSION_PATTERN = Pattern.compile("\\d+.\\d+.\\d+");

    private static void checkMinecraftVersionFormat(String s) {
        if (!MINECRAFT_VERSION_PATTERN.matcher(s)
                                      .matches()) {
            throw new InvalidVersionFormatException("'" + s + "' is not satisfies the version format '\\d+.\\d+.\\d+'");
        }
    }

    public static boolean is1_20_x() {
        return compareMinecraftVersion(Bukkit.getMinecraftVersion(),
                                       "1.19.99999") > 0 && compareMinecraftVersion(Bukkit.getMinecraftVersion(),
                                                                                    "1.21.0") < 0;
    }

    /**
     * Compares two Minecraft Version values numerically.
     *
     * @param x the first Minecraft Version to compare
     * @param y the second Minecraft Version to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code x < y}; and
     * a value greater than {@code 0} if {@code x > y}
     */
    public static int compareMinecraftVersion(String x, String y) {
        String normalizedX = MINECRAFT_WITHOUT_PATCH_VERSION_PATTERN.matcher(x)
                                                                    .matches() ? x + ".0" : x;
        String normalizedY = MINECRAFT_WITHOUT_PATCH_VERSION_PATTERN.matcher(y)
                                                                    .matches() ? y + ".0" : y;

        checkMinecraftVersionFormat(normalizedX);
        checkMinecraftVersionFormat(normalizedY);

        String[] xUnits = normalizedX.split("\\.");
        String[] yUnits = normalizedY.split("\\.");
        for (int i = 0; i < xUnits.length; i++) {
            int xn = Integer.parseInt(xUnits[i]);
            int yn = Integer.parseInt(yUnits[i]);

            if (xn < yn) {
                return -1;
            }
            if (xn > yn) {
                return 1;
            }
        }

        return 0;
    }

    public static class InvalidVersionFormatException extends RuntimeException {
        public InvalidVersionFormatException(String message) {
            super(message);
        }
    }

    private VersionUtil() {
    }
}
