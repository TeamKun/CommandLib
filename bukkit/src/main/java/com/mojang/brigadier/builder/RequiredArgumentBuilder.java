package com.mojang.brigadier.builder;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;

public class RequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>> {
    public static <S, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type) {
        return null;
    }

    public RequiredArgumentBuilder<S, T> suggests(SuggestionProvider<S> provider) {
        return this;
    }

    public CommandNode<S> build() {
        return null;
    }
}
