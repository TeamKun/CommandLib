package net.kunmc.lab.commandlib.util.nms.core;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSParticleParam extends MinecraftClass {
    public NMSParticleParam(Object handle) {
        super(handle, "ParticleParam", "core.particles.ParticleParam", "core.particles.ParticleOptions");
    }

    public NMSParticle getParticle() {
        return new NMSParticle(invokeMethod("getParticle", "b", "getType"));
    }
}
