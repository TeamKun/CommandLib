package net.kunmc.lab.commandlib.util.nms.world.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.world.NMSVec3D;

public class NMSVec3D_v1_20_5 extends NMSVec3D {
    public NMSVec3D_v1_20_5(Object handle) {
        super(handle, "world.phys.Vec3");
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
