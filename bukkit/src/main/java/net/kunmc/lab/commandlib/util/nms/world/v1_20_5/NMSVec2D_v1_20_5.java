package net.kunmc.lab.commandlib.util.nms.world.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.world.NMSVec2D;

public class NMSVec2D_v1_20_5 extends NMSVec2D {
    public NMSVec2D_v1_20_5(Object handle) {
        super(handle, "world.phys.Vec2");
    }

    @Override
    public float x() {
        return getValue(Float.class, "x");
    }

    @Override
    public float y() {
        return getValue(Float.class, "y");
    }
}
