package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ArgumentVec3 implements ArgumentType<Object> {
    public static ArgumentVec3 a() {
        return null;
    }

    public static Vec3D a(CommandContext<CommandListenerWrapper> commandContext, String s) throws CommandSyntaxException {
        return null;
    }

    @Override
    public Object parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }
}
