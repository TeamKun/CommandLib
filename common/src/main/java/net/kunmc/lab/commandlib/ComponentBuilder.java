package net.kunmc.lab.commandlib;

import org.jetbrains.annotations.NotNull;

public abstract class ComponentBuilder<E, T extends ComponentBuilder<E, T>> {
    protected E component;

    public ComponentBuilder(@NotNull E component) {
        this.component = component;
    }

    public abstract T color(int rgb);

    public abstract T italic();

    public final E build() {
        return this.component;
    }
}
