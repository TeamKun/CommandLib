package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilder;

final class TestTranslatableComponentBuilder extends TranslatableComponentBuilder<String, String, TestTranslatableComponentBuilder> {
    TestTranslatableComponentBuilder(String key) {
        super(key);
    }

    @Override
    public TestTranslatableComponentBuilder color(int rgb) {
        return this;
    }

    @Override
    public TestTranslatableComponentBuilder italic() {
        return this;
    }

    @Override
    public TestTranslatableComponentBuilder append(String component) {
        this.component += component;
        return this;
    }
}
