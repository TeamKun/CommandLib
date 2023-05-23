package net.kunmc.lab.commandlib.util.nms.command;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class NMSCommandSourceStack extends MinecraftClass {
    public NMSCommandSourceStack(Object handle) {
        super(handle, "CommandListenerWrapper", "commands.CommandSourceStack");
    }

    public CommandSender getBukkitSender() {
        return ((CommandSender) invokeMethod("getBukkitSender"));
    }

    public Entity getBukkitEntity() {
        return ((Entity) invokeMethod("getBukkitEntity"));
    }

    public World getBukkitWorld() {
        return ((World) invokeMethod("getBukkitWorld"));
    }

    public Location getBukkitLocation() {
        return ((Location) invokeMethod("getBukkitLocation"));
    }
}
