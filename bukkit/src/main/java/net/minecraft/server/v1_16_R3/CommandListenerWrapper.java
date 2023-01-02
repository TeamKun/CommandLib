package net.minecraft.server.v1_16_R3;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class CommandListenerWrapper {
    public boolean hasPermission(int level) {
        return false;
    }

    public CommandSender getBukkitSender() {
        return null;
    }

    public Entity getBukkitEntity() {
        return null;
    }

    public World getBukkitWorld() {
        return null;
    }

    public Location getBukkitLocation() {
        return null;
    }
}
