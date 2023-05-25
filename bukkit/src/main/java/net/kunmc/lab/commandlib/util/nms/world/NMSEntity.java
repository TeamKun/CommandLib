package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import org.bukkit.entity.Entity;

public class NMSEntity extends MinecraftClass {
    public NMSEntity(Object handle) {
        super(handle, "Entity", "world.entity.Entity");
    }

    public Entity getBukkitEntity() {
        return ((Entity) invokeMethod("getBukkitEntity"));
    }
}
