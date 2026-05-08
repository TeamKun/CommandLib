package net.kunmc.lab.commandlib.suggestion;

import net.kunmc.lab.commandlib.CommonCommandContext;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface AsyncSuggestionAction<C extends CommonCommandContext<?, ?>> {
    CompletionStage<Void> accept(SuggestionBuilder<C> suggestionBuilder);
}
