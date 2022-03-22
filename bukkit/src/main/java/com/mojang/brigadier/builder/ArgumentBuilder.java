package com.mojang.brigadier.builder;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ArgumentBuilder<S, T extends ArgumentBuilder<S, T>> {
    public T then(final ArgumentBuilder<S, ?> argument) {
        return null;
    }

    public T then(final CommandNode<S> argument) {
        return null;
    }

    public Collection<CommandNode<S>> getArguments() {
        return null;
    }

    public T executes(final Command<S> command) {
        return null;
    }

    public Command<S> getCommand() {
        return null;
    }

    public T requires(final Predicate<S> requirement) {
        return null;
    }

    public T redirect(final CommandNode<S> target) {
        return null;
    }

    public abstract CommandNode<S> build();
}
