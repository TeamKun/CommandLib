package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kunmc.lab.commandlib.command.CommandExecutor;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.suggestion.AsyncSuggestionAction;
import net.kunmc.lab.commandlib.suggestion.SuggestionAction;
import net.kunmc.lab.commandlib.suggestion.SuggestionBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

final class ArgumentCommandNodeCreator<S, T, C extends AbstractCommandContext<S, T>> {
    private final PlatformAdapter<S, T, C, ?, ?> platformAdapter = PlatformAdapter.get();
    private final Arguments<C> arguments;
    private final List<Arguments<C>> executorArguments;

    ArgumentCommandNodeCreator(Arguments<C> arguments, List<Arguments<C>> executorArguments) {
        this.arguments = arguments;
        this.executorArguments = List.copyOf(executorArguments);
    }

    private RequiredArgumentBuilder<S, ?> buildArgument(CommonArgument<?, C> argument,
                                                        CommandExecutor<C> helpAction,
                                                        CommonCommand<C, ?, ?> parent) {
        RequiredArgumentBuilder<S, ?> builder = RequiredArgumentBuilder.argument(argument.name(), argument.type());

        SuggestionAction<C> suggestionAction = argument.suggestionAction();
        AsyncSuggestionAction<C> asyncSuggestionAction = argument.asyncSuggestionAction();
        if (suggestionAction != null || asyncSuggestionAction != null) {
            builder.suggests((context, sb) -> {
                try {
                    C ctx = platformAdapter.createCommandContext(context);
                    try {
                        arguments.parse(ctx);
                    } catch (ArgumentParseException ignored) {
                    }

                    SuggestionBuilder<C> suggestionBuilder = new SuggestionBuilder<>(ctx);
                    if (suggestionAction != null) {
                        suggestionAction.accept(suggestionBuilder);
                    }

                    CompletionStage<Void> customStage = asyncSuggestionAction == null ? CompletableFuture.completedFuture(
                            null) : asyncSuggestionAction.accept(suggestionBuilder);
                    CompletableFuture<Void> customFuture = customStage.toCompletableFuture()
                                                                      .thenRun(() -> {
                                                                          suggestionBuilder.build()
                                                                                           .forEach(s -> s.suggest(sb));
                                                                      });

                    CompletableFuture<Suggestions> defaultFuture = argument.isDisplayDefaultSuggestions() ? argument.type()
                                                                                                                    .listSuggestions(
                                                                                                                            context,
                                                                                                                            sb)
                                                                                                                    .toCompletableFuture() : Suggestions.empty();

                    return customFuture.thenCombine(defaultFuture, (ignored, suggestions) -> sb.build());
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw e;
                }
            });
        }

        builder.executes(new CommandRunner<>(platformAdapter,
                                             executorArguments,
                                             parent.options(),
                                             parent.prerequisite(),
                                             helpAction,
                                             parent.preprocess(),
                                             argument.executor(),
                                             argument.uncaughtExceptionHandlers()));

        return builder;
    }

    private List<ArgumentCommandNode<S, ?>> toCommandNodes(CommandExecutor<C> helpAction,
                                                           CommonCommand<C, ?, ?> parent) {
        return arguments.stream()
                        .map(x -> buildArgument(x, helpAction, parent))
                        .map(RequiredArgumentBuilder::build)
                        .collect(Collectors.toList());
    }

    ArgumentCommandNode<S, ?> build(CommandExecutor<C> helpAction,
                                    CommonCommand<C, ?, ?> parent,
                                    Collection<LiteralCommandNode<S>> terminalChildren) {
        List<ArgumentCommandNode<S, ?>> nodes = toCommandNodes(helpAction, parent);
        if (nodes.isEmpty()) {
            return null;
        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            nodes.get(i)
                 .addChild(nodes.get(i + 1));
        }

        for (var child : terminalChildren) {
            // Append sub command nodes
            nodes.get(nodes.size() - 1)
                 .addChild(child);
        }

        return nodes.get(0);
    }
}
