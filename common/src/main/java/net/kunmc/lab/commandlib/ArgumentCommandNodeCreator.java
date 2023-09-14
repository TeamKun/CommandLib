package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.List;
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

        if (argument.suggestionAction() != null) {
            builder.suggests((context, sb) -> {
                try {
                    C ctx = platformAdapter.createCommandContext(context);
                    try {
                        arguments.parse(ctx);
                    } catch (IncorrectArgumentInputException ignored) {
                    }

                    SuggestionBuilder<C> suggestionBuilder = new SuggestionBuilder<>(ctx);
                    argument.suggestionAction()
                            .accept(suggestionBuilder);
                    suggestionBuilder.build()
                                     .forEach(s -> {
                                         s.suggest(sb);
                                     });
                    if (argument.isDisplayDefaultSuggestions()) {
                        argument.type()
                                .listSuggestions(context, sb)
                                .thenAccept(x -> {
                                    x.getList()
                                     .forEach(s -> {
                                         sb.suggest(s.getText(), s.getTooltip());
                                     });
                                });
                    }

                    return sb.buildFuture();
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw e;
                }
            });
        }

        builder.executes(new CommandExecutor<>(platformAdapter,
                                               arguments,
                                               parent.prerequisite(),
                                               argument::isContextActionUndefined,
                                               helpAction,
                                               parent.preprocess(),
                                               argument.contextAction()));

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
