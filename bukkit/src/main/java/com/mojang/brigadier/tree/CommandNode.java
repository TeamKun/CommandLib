package com.mojang.brigadier.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public abstract class CommandNode<S> {
    public void removeCommand(String name) {
    }

    public Command<S> getCommand() {
        return null;
    }

    public Collection<CommandNode<S>> getChildren() {
        return null;
    }

    public CommandNode<S> getChild(String name) {
        return null;
    }

    public CommandNode<S> getRedirect() {
        return null;
    }

    public void addChild(CommandNode<S> node) {
    }

    public abstract String getName();

    public abstract String getUsageText();

    public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) throws CommandSyntaxException;

    public abstract ArgumentBuilder<S, ?> createBuilder();
}
