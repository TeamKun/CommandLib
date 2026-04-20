package net.kunmc.lab.commandlib.util.nms.resources.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;
import net.kunmc.lab.commandlib.util.nms.resources.NMSCraftParticle;
import org.bukkit.Particle;

public class NMSCraftParticle_v1_16_0 extends NMSCraftParticle {
    public NMSCraftParticle_v1_16_0() {
        super(null, "CraftParticle");
    }

    @Override
    public Particle toBukkit(NMSParticleParam nms) {
        return ((Particle) invokeStaticMethod(new String[]{"toBukkit"},
                                              new Class<?>[]{nms.getFoundClass()},
                                              nms.getHandle()));
    }
}
