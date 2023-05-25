package net.kunmc.lab.commandlib.util.nms.core;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSParticle extends MinecraftClass {
    public NMSParticle(Object handle) {
        super(handle, "Particle", "core.particles.Particle", "core.particles.ParticleType");
    }
}
