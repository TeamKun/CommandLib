package net.kunmc.lab.commandlib.util.nms.world.v1_17_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSVec3D;

public class NMSVec3D_v1_17_0 extends NMSVec3D {
    public NMSVec3D_v1_17_0(Object handle) {
        super(handle, "world.phys.Vec3D");
    }

    @Override
    public double x() {
        return getValue(Double.class, "b");
    }

    @Override
    public double y() {
        return getValue(Double.class, "c");
    }

    @Override
    public double z() {
        return getValue(Double.class, "d");
    }
}
