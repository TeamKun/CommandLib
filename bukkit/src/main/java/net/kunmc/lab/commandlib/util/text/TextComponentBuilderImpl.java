package net.kunmc.lab.commandlib.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public class TextComponentBuilderImpl extends TextComponentBuilder<Component, TextComponentBuilderImpl> {
    public TextComponentBuilderImpl(@NotNull String text) {
        super(Component.text(text));
    }

    @Override
    public TextComponentBuilderImpl color(int rgb) {
        component = component.color(TextColor.color(rgb));
        return this;
    }

    @Override
    public TextComponentBuilderImpl italic() {
        component = component.decorate(TextDecoration.ITALIC);
        return this;
    }

    @Override
    public TextComponentBuilderImpl append(ComponentBuilder<?, ?> builder) {
        component = component.append(((Component) builder.build()));
        return this;
    }
}
