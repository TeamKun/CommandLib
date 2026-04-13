package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

final class ArgumentCommandNodeCreator<S, T, C extends AbstractCommandContext<S, T>> {
    private final PlatformAdapter<S, T, C, ?, ?> platformAdapter = PlatformAdapter.get();
    private final Arguments<C> arguments;

    ArgumentCommandNodeCreator(Arguments<C> arguments) {
        this.arguments = arguments;
    }

    private RequiredArgumentBuilder<S, ?> buildArgument(CommonArgument<?, C> argument,
                                                        ContextAction<C> helpAction,
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

        builder.executes(new CommandExecutor<>(platformAdapter,
                                               arguments,
                                               parent.options(),
                                               parent.prerequisite(),
                                               helpAction,
                                               parent.preprocess(),
                                               argument.contextAction(),
                                               argument.uncaughtExceptionHandlers()));

        return builder;
    }

    private List<ArgumentCommandNode<S, ?>> toCommandNodes(ContextAction<C> helpAction, CommonCommand<C, ?, ?> parent) {
        return arguments.stream()
                        .map(x -> buildArgument(x, helpAction, parent))
                        .map(RequiredArgumentBuilder::build)
                        .collect(Collectors.toList());
    }

    ArgumentCommandNode<S, ?> build(ContextAction<C> helpAction, CommonCommand<C, ?, ?> parent) {
        List<ArgumentCommandNode<S, ?>> nodes = toCommandNodes(helpAction, parent);
        if (nodes.isEmpty()) {
            return null;
        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            nodes.get(i)
                 .addChild(nodes.get(i + 1));
        }

        return nodes.get(0);
    }
}
