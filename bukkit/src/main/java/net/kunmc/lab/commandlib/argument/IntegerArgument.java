package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class IntegerArgument extends CommonIntegerArgument<CommandContext> {
    public IntegerArgument(String name) {
        super(name);
    }

    public IntegerArgument(String name, Consumer<Option<Integer, CommandContext>> options) {
        super(name, options);
    }

    public IntegerArgument(String name, Integer min, Integer max) {
        super(name, min, max);
    }

    public IntegerArgument(String name, Consumer<Option<Integer, CommandContext>> options, Integer min, Integer max) {
        super(name, options, min, max);
    }
}
