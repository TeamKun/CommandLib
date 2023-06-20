package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.Nameable;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NameableObjectArgument<T extends Nameable> extends CommonNameableObjectArgument<T, CommandContext> {
    public NameableObjectArgument(String name, Collection<? extends T> candidates) {
        super(name, candidates);
    }

    public NameableObjectArgument(String name,
                                  Collection<? extends T> candidates,
                                  Consumer<Option<T, CommandContext>> options) {
        super(name, candidates, options);
    }

    public NameableObjectArgument(String name, Supplier<Collection<? extends T>> candidatesSupplier) {
        super(name, candidatesSupplier);
    }

    public NameableObjectArgument(String name,
                                  Supplier<Collection<? extends T>> candidatesSupplier,
                                  Consumer<Option<T, CommandContext>> options) {
        super(name, candidatesSupplier, options);
    }
}
