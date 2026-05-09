package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.suggestion.SuggestionBuilder;
import net.kunmc.lab.commandlib.util.StringUtil;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

import java.util.Iterator;

final class NamespacedKeyArgumentSupport {
    private NamespacedKeyArgumentSupport() {
    }

    static NamespacedKey parseKey(String value) throws ArgumentParseException {
        NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : NamespacedKey.minecraft(value);
        if (key == null) {
            throw new ArgumentParseException(x -> x.sendFailure(value + " is not valid namespaced key"));
        }
        return key;
    }

    static void suggestKeys(SuggestionBuilder<?> builder, Iterator<? extends Keyed> keyedValues) {
        while (keyedValues.hasNext()) {
            suggestKey(builder,
                       keyedValues.next()
                                  .getKey());
        }
    }

    static void suggestKeys(SuggestionBuilder<?> builder, Iterable<? extends Keyed> keyedValues) {
        for (Keyed keyed : keyedValues) {
            suggestKey(builder, keyed.getKey());
        }
    }

    static void suggestKey(SuggestionBuilder<?> builder, NamespacedKey key) {
        String value = key.toString();
        if (builder.getLatestInput()
                   .isEmpty() || StringUtil.containsIgnoreCase(value, builder.getLatestInput())) {
            builder.suggest(value);
        }
    }
}
