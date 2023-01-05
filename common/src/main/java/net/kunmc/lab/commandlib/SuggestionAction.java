package net.kunmc.lab.commandlib;

import java.util.function.Consumer;

public interface SuggestionAction<T extends AbstractCommandContext<?, ?>> extends Consumer<SuggestionBuilder<T>> {
}
