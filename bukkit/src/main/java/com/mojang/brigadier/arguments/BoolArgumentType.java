package com.mojang.brigadier.arguments;

import com.mojang.brigadier.context.CommandContext;

public class BoolArgumentType implements ArgumentType<Boolean> {
    public static BoolArgumentType bool() {
        return new BoolArgumentType();
    }

    public static boolean getBool(CommandContext<?> context, String name) {
        return false;
    }
}
