package net.kunmc.lab.commandlib.util.nms.resources;

import net.kunmc.lab.commandlib.util.nms.CraftBukkitClass;
import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;
import org.bukkit.Particle;

public class NMSCraftParticle extends CraftBukkitClass {
    public NMSCraftParticle() {
        super(null, "CraftParticle");
    }

    public Particle toBukkit(NMSParticleParam nms) {
        return ((Particle) invokeMethod(new String[]{"toBukkit"},
                                        new Class<?>[]{nms.getFoundClass()},
                                        nms.getHandle()));
    }
}
