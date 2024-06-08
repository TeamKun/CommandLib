package net.kunmc.lab.commandlib.util.nms.core.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.core.NMSParticle;
import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;

public class NMSParticleParam_v1_16_0 extends NMSParticleParam {
    public NMSParticleParam_v1_16_0(Object handle) {
        super(handle, "ParticleParam");
    }

    @Override
    public NMSParticle getParticle() {
        return NMSParticle.create(invokeMethod("getParticle"));
    }
}
