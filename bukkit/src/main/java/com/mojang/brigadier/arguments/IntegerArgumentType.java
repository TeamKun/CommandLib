package com.mojang.brigadier.arguments;

import com.mojang.brigadier.context.CommandContext;

public class IntegerArgumentType implements ArgumentType {
    public static IntegerArgumentType integer(int min, int max) {
        return null;
    }

    public static int getInteger(CommandContext<?> context, String name) {
        return 0;
    }
}
