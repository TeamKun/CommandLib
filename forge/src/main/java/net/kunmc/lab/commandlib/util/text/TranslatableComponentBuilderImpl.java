package net.kunmc.lab.commandlib.util.text;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

public class TranslatableComponentBuilderImpl extends TranslatableComponentBuilder<TranslationTextComponent, TranslatableComponentBuilderImpl> {
    public TranslatableComponentBuilderImpl(@NotNull String key) {
        super(new TranslationTextComponent(key));
    }

    @Override
    public TranslatableComponentBuilderImpl color(int rgb) {
        component.setStyle(component.getStyle()
                                    .setColor(Color.fromInt(rgb)));
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl italic() {
        component.mergeStyle(TextFormatting.ITALIC);
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl append(ComponentBuilder<?, ?> builder) {
        component.appendSibling(((ITextComponent) builder.build()));
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl key(@NotNull String key) {
        TranslationTextComponent newComponent = new TranslationTextComponent(key, component.getFormatArgs());
        newComponent.setStyle(component.getStyle());
        component = newComponent;
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl args(@NotNull Object... args) {
        TranslationTextComponent newComponent = new TranslationTextComponent(component.getKey(), args);
        newComponent.setStyle(component.getStyle());
        component = newComponent;
        return this;
    }
}
