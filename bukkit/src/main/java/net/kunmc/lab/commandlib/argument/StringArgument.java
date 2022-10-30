package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.function.Consumer;

public class StringArgument extends Argument<String> {
    public StringArgument(String name) {
        this(name, option -> {
        });
    }

    public StringArgument(String name, Consumer<Option<String>> options) {
        this(name, options, Type.PHRASE_QUOTED);
    }

    public StringArgument(String name, Type type) {
        this(name, option -> {
        }, type);
    }

    public StringArgument(String name, Consumer<Option<String>> options, Type type) {
        super(name, type.type);
        setOptions(options);
    }

    @Override
    public String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    public String parse(CommandContext<CommandListenerWrapper> ctx) {
        return StringArgumentType.getString(ctx, name);
    }

    public enum Type {
        WORD(StringArgumentType.word()),
        PHRASE_QUOTED(StringArgumentType.string()),
        PHRASE(StringArgumentType.greedyString());

        final StringArgumentType type;

        Type(StringArgumentType type) {
            this.type = type;
        }
    }
}
