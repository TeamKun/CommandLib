package com.mojang.brigadier.tree;

import com.google.common.collect.Maps;
import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public abstract class CommandNode<S> implements Comparable<CommandNode<S>> {
    private final Map<String, CommandNode<S>> children = Maps.newTreeMap();
    private final Map<String, LiteralCommandNode<S>> literals = Maps.newLinkedHashMap();
    private final Map<String, ArgumentCommandNode<S, ?>> arguments = Maps.newLinkedHashMap();
    public Predicate<S> requirement;
    private final CommandNode<S> redirect;
    private final RedirectModifier<S> modifier;
    private final boolean forks;
    private Command<S> command;

    public void removeCommand(String name) {
        this.children.remove(name);
        this.literals.remove(name);
        this.arguments.remove(name);
    }

    protected CommandNode(Command<S> command,
                          Predicate<S> requirement,
                          CommandNode<S> redirect,
                          RedirectModifier<S> modifier,
                          boolean forks) {
        this.command = command;
        this.requirement = requirement;
        this.redirect = redirect;
        this.modifier = modifier;
        this.forks = forks;
    }

    public Command<S> getCommand() {
        return this.command;
    }

    public Collection<CommandNode<S>> getChildren() {
        return this.children.values();
    }

    public CommandNode<S> getChild(String name) {
        return this.children.get(name);
    }

    public CommandNode<S> getRedirect() {
        return this.redirect;
    }

    public RedirectModifier<S> getRedirectModifier() {
        return modifier;
    }

    public synchronized boolean canUse(S source) {
        return false;
    }

    public void addChild(CommandNode<S> node) {
    }

    public void findAmbiguities(AmbiguityConsumer<S> consumer) {
    }

    protected abstract boolean isValidInput(String var1);

    public boolean equals(Object o) {
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public Predicate<S> getRequirement() {
        return requirement;
    }

    public abstract String getName();

    public abstract String getUsageText();

    public abstract void parse(StringReader var1, CommandContextBuilder<S> var2) throws CommandSyntaxException;

    public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1,
                                                                   SuggestionsBuilder var2) throws CommandSyntaxException;

    public abstract ArgumentBuilder<S, ?> createBuilder();

    protected abstract String getSortedKey();

    public Collection<? extends CommandNode<S>> getRelevantNodes(StringReader input) {
        return null;
    }

    public int compareTo(CommandNode<S> o) {
        return 0;
    }

    public boolean isFork() {
        return forks;
    }

    public abstract Collection<String> getExamples();
}
