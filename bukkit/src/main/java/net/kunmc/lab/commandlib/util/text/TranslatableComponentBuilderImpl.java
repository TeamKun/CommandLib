package net.kunmc.lab.commandlib.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TranslatableComponentBuilderImpl extends TranslatableComponentBuilder<TranslatableComponent, TranslatableComponentBuilderImpl> {
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
        component = component.style(Style.style(TextDecoration.ITALIC));
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl key(@NotNull String key) {
        component = component.key(key);
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl args(@NotNull Object... args) {
        component = component.args(Arrays.stream(args)
                                         .map(Object::toString)
                                         .map(Component::text)
                                         .collect(Collectors.toList()));
        return this;
    }
}
