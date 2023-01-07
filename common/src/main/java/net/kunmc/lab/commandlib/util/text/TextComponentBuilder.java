package net.kunmc.lab.commandlib.util.text;

public abstract class TextComponentBuilder<E, T extends TextComponentBuilder<E, T>> extends ComponentBuilder<E, T> {
    public TextComponentBuilder(E component) {
        super(component);
    }
}
