package net.kunmc.lab.commandlib.util;

import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

public final class TextColorUtil {
    public static final TextColor RED = TextColor.color(ChatColor.RED.getColor()
                                                                     .getRGB());

    private TextColorUtil() {
    }
}
