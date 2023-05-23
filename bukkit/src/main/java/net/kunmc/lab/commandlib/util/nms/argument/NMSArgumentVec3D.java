package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.world.NMSVec3D;

public class NMSArgumentVec3D extends MinecraftClass {
    public NMSArgumentVec3D() {
        super(null,
              "ArgumentVec3",
              "commands.arguments.coordinates.Vec3Argument",
              "commands.arguments.coordinates.ArgumentVec3");
    }

    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeMethod("a", "vec3"));
    }

    public NMSVec3D parse(CommandContext<?> ctx, String name) {
        return new NMSVec3D(invokeMethod("a", "getVec3", ctx, name));
    }
}
