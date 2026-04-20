package net.kunmc.lab.commandlib.util.nms.core.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.core.NMSParticle;
import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;

public class NMSParticleParam_v1_20_5 extends NMSParticleParam {
    public NMSParticleParam_v1_20_5(Object handle) {
        super(handle, "core.particles.ParticleOptions");
    }

    @Override
    public NMSParticle getParticle() {
        return NMSParticle.create(invokeMethod("getType"));
    }
}
