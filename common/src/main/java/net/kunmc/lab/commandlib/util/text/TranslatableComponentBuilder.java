package net.kunmc.lab.commandlib.util.text;

import org.jetbrains.annotations.NotNull;

public abstract class TranslatableComponentBuilder<B, E extends B, T extends TranslatableComponentBuilder<B, E, T>> extends ComponentBuilder<B, E, T> {
    public TranslatableComponentBuilder(@NotNull E component) {
        super(component);
    }
}
