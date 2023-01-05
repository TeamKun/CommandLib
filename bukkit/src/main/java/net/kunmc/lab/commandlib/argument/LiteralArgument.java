package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.ContextAction;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LiteralArgument extends AbstractLiteralArgument<CommandContext> {
    public LiteralArgument(String name, Collection<String> literals) {
        super(name, literals);
    }

    public LiteralArgument(String name, Collection<String> literals, Consumer<Option<String, CommandContext>> options) {
        super(name, literals, options);
    }

    public LiteralArgument(String name, Supplier<Collection<String>> literalsSupplier) {
        super(name, literalsSupplier);
    }

    public LiteralArgument(String name,
                           Supplier<Collection<String>> literalsSupplier,
                           Consumer<Option<String, CommandContext>> options) {
        super(name, literalsSupplier, options);
    }

    public LiteralArgument(String name,
                           Supplier<Collection<String>> literalsSupplier,
                           ContextAction<CommandContext> contextAction) {
        super(name, literalsSupplier, contextAction);
    }
}
