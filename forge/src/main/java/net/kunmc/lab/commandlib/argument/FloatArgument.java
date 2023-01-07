package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class FloatArgument extends CommonFloatArgument<CommandContext> {
    public FloatArgument(String name) {
        super(name);
    }

    public FloatArgument(String name, Consumer<Option<Float, CommandContext>> options) {
        super(name, options);
    }

    public FloatArgument(String name, Float min, Float max) {
        super(name, min, max);
    }

    public FloatArgument(String name, Consumer<Option<Float, CommandContext>> options, Float min, Float max) {
        super(name, options, min, max);
    }
}
