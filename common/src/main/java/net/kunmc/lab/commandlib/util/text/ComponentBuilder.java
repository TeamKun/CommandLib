package net.kunmc.lab.commandlib.util.text;

import org.jetbrains.annotations.NotNull;

public abstract class ComponentBuilder<B, E extends B, T extends ComponentBuilder<B, E, T>> {
    protected E component;

    public ComponentBuilder(@NotNull E component) {
        this.component = component;
    }

    public abstract T color(int rgb);

    public abstract T italic();

    public abstract T append(B component);

    public final B build() {
        return this.component;
    }
}
