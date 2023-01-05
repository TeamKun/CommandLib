package net.kunmc.lab.commandlib.util.text;

import net.kunmc.lab.commandlib.ComponentBuilder;

public abstract class TextComponentBuilder<E, T extends TextComponentBuilder<E, T>> extends ComponentBuilder<E, T> {
    public TextComponentBuilder(E component) {
        super(component);
    }
}
