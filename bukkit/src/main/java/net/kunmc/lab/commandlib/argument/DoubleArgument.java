package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class DoubleArgument extends CommonDoubleArgument<CommandContext> {
    public DoubleArgument(String name) {
        super(name);
    }

    public DoubleArgument(String name, Consumer<Option<Double, CommandContext>> options) {
        super(name, options);
    }

    public DoubleArgument(String name, Double min, Double max) {
        super(name, min, max);
    }

    public DoubleArgument(String name, Consumer<Option<Double, CommandContext>> options, Double min, Double max) {
        super(name, options, min, max);
    }
}
