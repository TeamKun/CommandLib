package net.kunmc.lab.commandlib.util.text;

import org.jetbrains.annotations.NotNull;

public abstract class ComponentBuilder<E, T extends ComponentBuilder<E, T>> {
    protected E component;

    public ComponentBuilder(@NotNull E component) {
        this.component = component;
    }

    public abstract T color(int rgb);

    public abstract T italic();

    public abstract T append(ComponentBuilder<?, ?> builder);

    public final E build() {
        return this.component;
    }
}
