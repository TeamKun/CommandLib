package net.kunmc.lab.commandlib.suggestion;

import net.kunmc.lab.commandlib.CommonCommandContext;

import java.util.function.Consumer;

public interface SuggestionAction<C extends CommonCommandContext<?, ?>> extends Consumer<SuggestionBuilder<C>> {
}
