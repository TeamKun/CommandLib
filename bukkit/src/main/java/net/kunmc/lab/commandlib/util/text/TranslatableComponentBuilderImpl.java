package net.kunmc.lab.commandlib.util.text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TranslatableComponentBuilderImpl extends TranslatableComponentBuilder<BaseComponent, TranslatableComponent, TranslatableComponentBuilderImpl> {
    public TranslatableComponentBuilderImpl(@NotNull String key) {
        super(new TranslatableComponent(key));
    }

    @Override
    public TranslatableComponentBuilderImpl color(int rgb) {
        component.setColor(ChatColor.of(new Color(rgb)));
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl italic() {
        component.setItalic(true);
        return this;
    }

    @Override
    public TranslatableComponentBuilderImpl append(BaseComponent component) {
        this.component.addExtra(component);
        return this;
    }
}
