package com.mojang.brigadier.builder;

import com.mojang.brigadier.tree.CommandNode;

public class LiteralArgumentBuilder<S> extends ArgumentBuilder<S, LiteralArgumentBuilder<S>> {
    public static <S> LiteralArgumentBuilder<S> literal(String s) {
        return null;
    }

    @Override
    public CommandNode<S> build() {
        return null;
    }
}
