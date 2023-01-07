package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

import java.util.Map;
import java.util.function.Consumer;

public class ObjectArgument<T> extends CommonObjectArgument<T, CommandContext> {
    public ObjectArgument(String name, Map<String, ? extends T> nameToObjectMap) {
        super(name, nameToObjectMap);
    }

    public ObjectArgument(String name,
                          Map<String, ? extends T> nameToObjectMap,
                          Consumer<Option<T, CommandContext>> options) {
        super(name, nameToObjectMap, options);
    }
}
