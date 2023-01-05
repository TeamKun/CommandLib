package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.Nameable;

import java.util.Collection;
import java.util.function.Consumer;

public class NameableObjectArgument<T extends Nameable> extends AbstractNameableObjectArgument<T, CommandContext> {
    public NameableObjectArgument(String name, Collection<? extends T> candidates) {
        super(name, candidates);
    }

    public NameableObjectArgument(String name,
                                  Collection<? extends T> candidates,
                                  Consumer<Option<T, CommandContext>> options) {
        super(name, candidates, options);
    }
}
