package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.util.text.TextComponentBuilder;

final class TestTextComponentBuilder extends TextComponentBuilder<String, String, TestTextComponentBuilder> {
    TestTextComponentBuilder(String component) {
        super(component);
    }

    @Override
    public TestTextComponentBuilder color(int rgb) {
        return this;
    }

    @Override
    public TestTextComponentBuilder italic() {
        return this;
    }

    @Override
    public TestTextComponentBuilder append(String component) {
        this.component += component;
        return this;
    }
}
