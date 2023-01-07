package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.CommandContext;

import java.util.function.Consumer;

public class StringArgument extends CommonStringArgument<CommandContext> {
    public StringArgument(String name) {
        super(name);
    }

    public StringArgument(String name, Consumer<Option<String, CommandContext>> options) {
        super(name, options);
    }

    public StringArgument(String name, Type type) {
        super(name, type);
    }

    public StringArgument(String name, Consumer<Option<String, CommandContext>> options, Type type) {
        super(name, options, type);
    }

    // compatible class
    public static class Type extends CommonStringArgument.Type {
        public static final Type WORD = new Type(StringArgumentType.word());
        public static final Type PHRASE_QUOTED = new Type(StringArgumentType.string());
        public static final Type PHRASE = new Type(StringArgumentType.greedyString());

        private Type(StringArgumentType type) {
            super(type);
        }
    }
}
