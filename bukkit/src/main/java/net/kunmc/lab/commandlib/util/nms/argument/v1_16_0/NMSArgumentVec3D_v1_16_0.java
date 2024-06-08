package net.kunmc.lab.commandlib.util.nms.argument.v1_16_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentVec3D;
import net.kunmc.lab.commandlib.util.nms.world.NMSVec3D;

public class NMSArgumentVec3D_v1_16_0 extends NMSArgumentVec3D {
    public NMSArgumentVec3D_v1_16_0() {
        super(null, "ArgumentVec3");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("a"));
    }

    @Override
    protected NMSVec3D parseImpl(CommandContext<?> ctx, String name) {
        return NMSVec3D.create(invokeStaticMethod("a", ctx, name));
    }
}
