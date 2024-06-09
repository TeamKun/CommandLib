package net.kunmc.lab.commandlib.util.nms.command.v1_20_6;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandListenerWrapper;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import net.kunmc.lab.commandlib.util.nms.world.NMSVec2D;
import net.kunmc.lab.commandlib.util.nms.world.NMSVec3D;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class NMSCommandListenerWrapper_v1_20_6 extends NMSCommandListenerWrapper {
    public NMSCommandListenerWrapper_v1_20_6(Object handle) {
        super(handle, "commands.CommandSourceStack");
    }

    public CommandSender getBukkitSender() {
        return ((CommandSender) invokeMethod("getBukkitSender"));
    }

    public Entity getBukkitEntity() {
        return NMSEntity.create(invokeMethod("getEntity"))
                        .getBukkitEntity();
    }

    public World getBukkitWorld() {
        try {
            Object level = getValue("level");
            return ((World) ReflectionUtil.getMethodIncludingSuperclasses(level.getClass(), "getWorld")
                                          .invoke(level));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Location getBukkitLocation() {
        NMSVec3D pos = NMSVec3D.create(invokeMethod("getPosition"));
        World world = getBukkitWorld();
        NMSVec2D rot = NMSVec2D.create(invokeMethod("getRotation"));
        return new Location(world,
                            pos.x(),
                            pos.y(),
                            pos.z(),
                            rot != null ? rot.y() : 0.0F,
                            rot != null ? rot.x() : 0.0F);
    }
}
