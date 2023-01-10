package net.kunmc.lab.commandlib.util.text;

public abstract class TextComponentBuilder<B, E extends B, T extends TextComponentBuilder<B, E, T>> extends ComponentBuilder<B, E, T> {
    public TextComponentBuilder(E component) {
        super(component);
    }
}
