package net.kunmc.lab.commandlib;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Suggestion {
    private final String text;
    private final String tooltip;

    public Suggestion(@NotNull String text, @Nullable String tooltip) {
        this.text = Objects.requireNonNull(text);
        this.tooltip = tooltip;
    }

    public void suggest(SuggestionsBuilder sb) {
        if (tooltip != null) {
            sb.suggest(text, new LiteralMessage(tooltip));
        } else {
            sb.suggest(text);
        }
    }
}
