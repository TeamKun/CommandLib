package net.kunmc.lab.commandlib.util.nms.world.v1_17_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Entity;

public class NMSEntity_v1_17_0 extends NMSEntity {
    public NMSEntity_v1_17_0(Object handle) {
        super(handle, "world.entity.Entity");
    }

    @Override
    public Entity getBukkitEntity() {
        return ((Entity) invokeMethod("getBukkitEntity"));
    }
}
