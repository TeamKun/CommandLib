package net.kunmc.lab.commandlib;

import java.util.ArrayList;
import java.util.List;

public class SuggestionBuilder {
    private final List<Suggestion> suggestions = new ArrayList<>();

    public SuggestionBuilder suggest(String suggest) {
        suggestions.add(new Suggestion(suggest, null));
        return this;
    }

    public SuggestionBuilder suggest(String suggest, String tooltip) {
        suggestions.add(new Suggestion(suggest, tooltip));
        return this;
    }

    List<Suggestion> build() {
        return suggestions;
    }
}
