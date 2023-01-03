package net.kunmc.lab.commandlib.util;

import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

public final class TextColorUtil {
    public static final TextColor GREEN = fromChatColor(ChatColor.GREEN);
    public static final TextColor YELLOW = fromChatColor(ChatColor.YELLOW);
    public static final TextColor RED = fromChatColor(ChatColor.RED);

    public static TextColor fromChatColor(org.bukkit.ChatColor chatColor) {
        return fromChatColor(chatColor.asBungee());
    }

    public static TextColor fromChatColor(ChatColor chatColor) {
        return TextColor.color(chatColor.getColor()
                                        .getRGB());
    }

    private TextColorUtil() {
    }
}
