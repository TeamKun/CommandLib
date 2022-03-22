package net.kunmc.lab.commandlib;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class Suggestion {
    private final String text;
    private final String tooltip;

    public Suggestion(String text, String tooltip) {
        this.text = text;
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
