package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectArgument<T> extends CommonObjectArgument<T, CommandContext> {
    public ObjectArgument(String name, Map<String, ? extends T> nameToObjectMap) {
        super(name, nameToObjectMap);
    }

    public ObjectArgument(String name,
                          Map<String, ? extends T> nameToObjectMap,
                          Consumer<Option<T, CommandContext>> options) {
        super(name, nameToObjectMap, options);
    }

    public ObjectArgument(String name, Supplier<Map<String, ? extends T>> mapSupplier) {
        super(name, mapSupplier, option -> {
        });
    }

    public ObjectArgument(String name,
                          Supplier<Map<String, ? extends T>> mapSupplier,
                          Consumer<Option<T, CommandContext>> options) {
        super(name, mapSupplier, options);
    }
}
