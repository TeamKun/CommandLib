package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

public class FloatArgument extends CommonFloatArgument<CommandContext, FloatArgument> {
    public FloatArgument(String name) {
        super(name);
    }

    public FloatArgument(String name, float min, float max) {
        super(name, min, max);
    }
}
