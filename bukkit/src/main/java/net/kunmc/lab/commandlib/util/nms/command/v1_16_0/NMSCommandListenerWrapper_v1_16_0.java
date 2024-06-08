package net.kunmc.lab.commandlib.util.nms.command.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandListenerWrapper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class NMSCommandListenerWrapper_v1_16_0 extends NMSCommandListenerWrapper {
    public NMSCommandListenerWrapper_v1_16_0(Object handle) {
        super(handle, "CommandListenerWrapper");
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
