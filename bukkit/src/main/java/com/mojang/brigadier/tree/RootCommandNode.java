package com.mojang.brigadier.tree;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public class RootCommandNode<S> extends CommandNode<S> {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getUsageText() {
        return null;
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) throws CommandSyntaxException {
        return null;
    }

    @Override
    public ArgumentBuilder<S, ?> createBuilder() {
        return null;
    }
}
