package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentVec3D;
import net.kunmc.lab.commandlib.util.nms.world.NMSVec3D;

public class NMSArgumentVec3D_v1_20_5 extends NMSArgumentVec3D {
    public NMSArgumentVec3D_v1_20_5() {
        super(null, "commands.arguments.coordinates.Vec3Argument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("vec3"));
    }

    @Override
    protected NMSVec3D parseImpl(CommandContext<?> ctx, String name) {
        return NMSVec3D.create(invokeStaticMethod("getVec3", ctx, name));
    }
}
