package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class EnumArgument<T extends Enum<T>> extends CommonEnumArgument<T, CommandContext> {
    public EnumArgument(String name, Class<T> clazz) {
        super(name, clazz);
    }

    public EnumArgument(String name, Class<T> clazz, Consumer<Option<T, CommandContext>> options) {
        super(name, clazz, options);
    }
}
