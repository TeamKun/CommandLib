package com.mojang.brigadier.arguments;

import com.mojang.brigadier.context.CommandContext;

public class StringArgumentType implements ArgumentType {
    public static StringArgumentType word() {
        return null;
    }

    public static StringArgumentType string() {
        return null;
    }

    public static StringArgumentType greedyString() {
        return null;
    }

    public static String getString(CommandContext<?> context, String name) {
        return "";
    }
}
