package net.kunmc.lab.commandlib.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public class TranslatableComponentBuilderImpl extends TranslatableComponentBuilder<Component, TranslatableComponent, TranslatableComponentBuilderImpl> {
    public TranslatableComponentBuilderImpl(@NotNull String key) {
        super(Component.translatable(key));
    }

    @Override
    public TranslatableComponentBuilderImpl color(int rgb) {
        component = component.color(TextColor.color(rgb));
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl italic() {
        component = component.decoration(TextDecoration.ITALIC, true);
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl append(Component component) {
        this.component = this.component.append(component);
        return this;
    }
}
