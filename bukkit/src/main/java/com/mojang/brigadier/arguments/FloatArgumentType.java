package com.mojang.brigadier.arguments;

import com.mojang.brigadier.context.CommandContext;

public class FloatArgumentType implements ArgumentType {
    public static FloatArgumentType floatArg(float min, float max) {
        return null;
    }

    public static float getFloat(CommandContext<?> context, String name) {
        return 0.0F;
    }
}
