package net.kunmc.lab.commandlib;

import java.util.function.Consumer;

public interface SuggestionAction<C extends AbstractCommandContext<?, ?>> extends Consumer<SuggestionBuilder<C>> {
}
