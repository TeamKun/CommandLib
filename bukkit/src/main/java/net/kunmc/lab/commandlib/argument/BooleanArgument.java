package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class BooleanArgument extends CommonBooleanArgument<CommandContext> {
    public BooleanArgument(String name) {
        super(name);
    }

    public BooleanArgument(String name, Consumer<Option<Boolean, CommandContext>> options) {
        super(name, options);
    }
}
