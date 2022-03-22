package com.mojang.brigadier.arguments;

import com.mojang.brigadier.context.CommandContext;

public class DoubleArgumentType implements ArgumentType<Double> {
    private DoubleArgumentType(double minimum, double maximum) {
    }

    public static DoubleArgumentType doubleArg() {
        return null;
    }

    public static DoubleArgumentType doubleArg(double min) {
        return null;
    }

    public static DoubleArgumentType doubleArg(double min, double max) {
        return null;
    }

    public static double getDouble(CommandContext<?> context, String name) {
        return 0.0;
    }
}
