package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ArgumentParticle implements ArgumentType {
    public static ArgumentParticle a() {
        return null;
    }

    public static ParticleParam a(CommandContext ctx, String s) {
        return null;
    }

    @Override
    public Object parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }
}
