package net.kunmc.lab.commandlib;

import java.util.function.Consumer;

public interface ContextAction<C extends AbstractCommandContext<?, ?>> extends Consumer<C> {
}
