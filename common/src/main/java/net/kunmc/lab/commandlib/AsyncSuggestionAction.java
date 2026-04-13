package net.kunmc.lab.commandlib;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface AsyncSuggestionAction<C extends AbstractCommandContext<?, ?>> {
    CompletionStage<Void> accept(SuggestionBuilder<C> suggestionBuilder);
}
