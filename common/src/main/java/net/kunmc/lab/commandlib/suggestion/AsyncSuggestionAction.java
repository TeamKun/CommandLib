package net.kunmc.lab.commandlib.suggestion;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface AsyncSuggestionAction<C extends AbstractCommandContext<?, ?>> {
    CompletionStage<Void> accept(SuggestionBuilder<C> suggestionBuilder);
}
