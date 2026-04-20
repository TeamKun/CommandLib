package net.kunmc.lab.commandlib.util.nms.world.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Entity;

public class NMSEntity_v1_16_0 extends NMSEntity {
    public NMSEntity_v1_16_0(Object handle) {
        super(handle, "Entity");
    }

    @Override
    public Entity getBukkitEntity() {
        return ((Entity) invokeMethod("getBukkitEntity"));
    }
}
