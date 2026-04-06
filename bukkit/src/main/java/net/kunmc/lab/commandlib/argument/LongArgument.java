package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class LongArgument extends CommonLongArgument<CommandContext> {
    public LongArgument(String name) {
        super(name);
    }

    public LongArgument(String name, Consumer<Option<Long, CommandContext>> options) {
        super(name, options);
    }

    public LongArgument(String name, Long min, Long max) {
        super(name, min, max);
    }

    public LongArgument(String name, Consumer<Option<Long, CommandContext>> options, Long min, Long max) {
        super(name, options, min, max);
    }
}
