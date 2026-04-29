package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

public class IntegerArgument extends CommonIntegerArgument<CommandContext, IntegerArgument> {
    public IntegerArgument(String name) {
        super(name);
    }

    public IntegerArgument(String name, int min, int max) {
        super(name, min, max);
    }
}
