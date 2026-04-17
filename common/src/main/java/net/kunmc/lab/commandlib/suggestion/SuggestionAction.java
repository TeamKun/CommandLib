package net.kunmc.lab.commandlib.suggestion;

import net.kunmc.lab.commandlib.AbstractCommandContext;

import java.util.function.Consumer;

public interface SuggestionAction<C extends AbstractCommandContext<?, ?>> extends Consumer<SuggestionBuilder<C>> {
}
