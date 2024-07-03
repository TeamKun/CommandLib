package net.kunmc.lab.commandlib.util.bukkit;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitUtil {
    private static final Pattern PATTERN = Pattern.compile("^\\d+\\.\\d+(\\.\\d+)?");

    public static String getMinecraftVersion() {
        Matcher matcher = PATTERN.matcher(Bukkit.getBukkitVersion());
        matcher.find();
        return matcher.group();
    }

    private BukkitUtil() {
    }
}
