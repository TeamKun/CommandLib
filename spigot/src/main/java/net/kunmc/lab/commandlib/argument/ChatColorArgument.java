package net.kunmc.lab.commandlib.argument;

import org.bukkit.ChatColor;

public class ChatColorArgument extends EnumArgument<ChatColor> {
    public ChatColorArgument(String name) {
        super(name, ChatColor.class);
    }
}
