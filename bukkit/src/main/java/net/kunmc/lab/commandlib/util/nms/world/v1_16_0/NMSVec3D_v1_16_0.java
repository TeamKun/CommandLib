package net.kunmc.lab.commandlib.util.nms.world.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSVec3D;

public class NMSVec3D_v1_16_0 extends NMSVec3D {
    public NMSVec3D_v1_16_0(Object handle) {
        super(handle, "Vec3D");
    }

    @Override
    public double x() {
        return getValue(Double.class, "x");
    }

    @Override
    public double y() {
        return getValue(Double.class, "y");
    }

    @Override
    public double z() {
        return getValue(Double.class, "z");
    }
}
