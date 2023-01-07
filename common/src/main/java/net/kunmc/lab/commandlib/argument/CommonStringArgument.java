package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

import java.util.function.Consumer;

public class CommonStringArgument<C extends AbstractCommandContext<?, ?>> extends CommonArgument<String, C> {
    public CommonStringArgument(String name) {
        this(name, option -> {
        });
    }

    public CommonStringArgument(String name, Consumer<Option<String, C>> options) {
        this(name, options, Type.PHRASE_QUOTED);
    }

    public CommonStringArgument(String name, Type type) {
        this(name, option -> {
        }, type);
    }

    public CommonStringArgument(String name, Consumer<Option<String, C>> options, Type type) {
        super(name, type.type);
        setOptions(options);
    }

    @Override
    public final String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    public final String parse(C ctx) {
        return StringArgumentType.getString(ctx.getHandle(), name);
    }

    public static class Type {
        public static final Type WORD = new Type(StringArgumentType.word());
        public static final Type PHRASE_QUOTED = new Type(StringArgumentType.string());
        public static final Type PHRASE = new Type(StringArgumentType.greedyString());

        final StringArgumentType type;

        protected Type(StringArgumentType type) {
            this.type = type;
        }
    }
}
