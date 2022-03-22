package net.minecraft.server.v1_16_R3;

import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class CommandListenerWrapper {
    public boolean hasPermission(int level) {
        return false;
    }

    public CommandSender getBukkitSender() {
        return null;
    }

    public World getBukkitWorld() {
        return null;
    }
}
