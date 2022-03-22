package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.Message;

import java.util.concurrent.CompletableFuture;

public class SuggestionsBuilder {
    public SuggestionsBuilder suggest(String text) {
        return this;
    }

    public SuggestionsBuilder suggest(String text, Message tooltip) {
        return this;
    }

    public CompletableFuture<Suggestions> buildFuture() {
        return null;
    }
}
