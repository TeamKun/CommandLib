package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSVec3D extends MinecraftClass {
    public NMSVec3D(Object handle) {
        super(handle, "Vec3D", "world.phys.Vec3D", "world.phys.Vec3");
    }

    public double x() {
        return getValue(Double.class, "x", "c");
    }

    public double y() {
        return getValue(Double.class, "y", "d");
    }

    public double z() {
        return getValue(Double.class, "z", "e");
    }
}
