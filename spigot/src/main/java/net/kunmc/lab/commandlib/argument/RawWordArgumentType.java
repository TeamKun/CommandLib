package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTypeRegistrar;

import java.util.Arrays;
import java.util.Collection;

class RawWordArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("@p", "@../config/value", "hello");
    private static volatile boolean registered = false;

    static RawWordArgumentType rawWord() {
        return new RawWordArgumentType();
    }

    static void ensureRegistered() {
        if (registered) {
            return;
        }
        synchronized (RawWordArgumentType.class) {
            if (registered) {
                return;
            }
            registered = true;
            NMSArgumentTypeRegistrar.create()
                                    .registerAsGreedyString(RawWordArgumentType.class, RawWordArgumentType::rawWord);
        }
    }

    @Override
    public String parse(StringReader reader) {
        int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        return reader.getString()
                     .substring(start, reader.getCursor());
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
