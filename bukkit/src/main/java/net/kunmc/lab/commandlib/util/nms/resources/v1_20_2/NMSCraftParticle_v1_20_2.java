package net.kunmc.lab.commandlib.util.nms.resources.v1_20_2;

import net.kunmc.lab.commandlib.util.nms.core.NMSParticle;
import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;
import net.kunmc.lab.commandlib.util.nms.resources.NMSCraftParticle;
import org.bukkit.Particle;

public class NMSCraftParticle_v1_20_2 extends NMSCraftParticle {
    public NMSCraftParticle_v1_20_2() {
        super(null, "CraftParticle");
    }

    public Particle toBukkit(NMSParticleParam nms) {
        return ((Particle) invokeStaticMethod(new String[]{"minecraftToBukkit"},
                                              new Class<?>[]{NMSParticle.create(null).getFoundClass()},
                                              nms.getHandle()));
    }
}
