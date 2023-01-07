package net.kunmc.lab.commandlib.util.text;

import org.jetbrains.annotations.NotNull;

public abstract class TranslatableComponentBuilder<E, T extends TranslatableComponentBuilder<E, T>> extends ComponentBuilder<E, T> {
    public TranslatableComponentBuilder(@NotNull E component) {
        super(component);
    }

    public abstract T key(@NotNull String key);

    public abstract T args(@NotNull Object... args);
}
