package net.kunmc.lab.commandlib.util.text;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class ComponentBuilder<B, E extends B, T extends ComponentBuilder<B, E, T>> {
    protected E component;

    public ComponentBuilder(@NotNull E component) {
        this.component = Objects.requireNonNull(component);
    }

    public abstract T color(int rgb);

    public abstract T italic();

    public abstract T append(B component);

    @NotNull
    public final B build() {
        return Objects.requireNonNull(this.component);
    }
}
