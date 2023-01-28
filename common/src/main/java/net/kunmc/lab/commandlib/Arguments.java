package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.List;
import java.util.stream.Collectors;

final class Arguments<S, C extends AbstractCommandContext<S, ?>> {
    private final List<? extends CommonArgument<?, C>> arguments;
    private final PlatformAdapter<S, ?, C, ?, ?> platformAdapter;

    Arguments(List<? extends CommonArgument<?, C>> arguments, PlatformAdapter<S, ?, C, ?, ?> platformAdapter) {
        this.arguments = arguments;
        this.platformAdapter = platformAdapter;
    }

    void parse(C ctx) throws IncorrectArgumentInputException {
        for (CommonArgument<?, C> argument : arguments) {
            try {
                Object parsedArg = argument.parseInternal(ctx);
                ctx.addParsedArgument(argument.name, parsedArg);
            } catch (IllegalArgumentException ignored) {
                // 補完時に入力中の引数で例外が発生するため握りつぶす
            }
        }
    }

    String concatTagNames() {
        if (arguments.isEmpty()) {
            return "";
        }

        return arguments.stream()
                        .map(x -> String.format(ChatColorUtil.GRAY + "<" + ChatColorUtil.YELLOW + "%s" + ChatColorUtil.GRAY + ">",
                                                x.name()))
                        .collect(Collectors.joining(" "));
    }

    private RequiredArgumentBuilder<S, ?> buildArgument(CommonArgument<?, C> argument,
                                                        ContextAction<C> defaultAction,
                                                        CommonCommand<C, ?, ?> parent) {
        RequiredArgumentBuilder<S, ?> builder = RequiredArgumentBuilder.argument(argument.name(), argument.type());

        if (argument.suggestionAction() != null) {
            builder.suggests((ctx, sb) -> {
                C context = platformAdapter.createCommandContext(ctx);
                try {
                    parse(context);
                } catch (IncorrectArgumentInputException ignored) {
                }

                SuggestionBuilder<C> suggestionBuilder = new SuggestionBuilder<>(context);
                argument.suggestionAction()
                        .accept(suggestionBuilder);
                suggestionBuilder.build()
                                 .forEach(s -> {
                                     s.suggest(sb);
                                 });

                return sb.buildFuture();
            });
        }

        if (!argument.hasContextAction()) {
            argument.setContextAction(defaultAction);
        }

        builder.executes(ctx -> {
            C context = platformAdapter.createCommandContext(ctx);
            try {
                parse(context);
            } catch (IncorrectArgumentInputException e) {
                e.sendMessage(context);
                return 1;
            }

            if (!parent.preprocesses()
                       .stream()
                       .allMatch(x -> x.apply(context))) {
                return 1;
            }

            return ContextAction.executeWithStackTrace(context, argument.contextAction());
        });

        return builder;
    }

    int size() {
        return arguments.size();
    }

    private List<ArgumentCommandNode<S, ?>> toCommandNodes(ContextAction<C> defaultAction,
                                                           CommonCommand<C, ?, ?> parent) {
        return arguments.stream()
                        .map(x -> buildArgument(x, defaultAction, parent))
                        .map(RequiredArgumentBuilder::build)
                        .collect(Collectors.toList());
    }

    ArgumentCommandNode<S, ?> build(ContextAction<C> defaultAction, CommonCommand<C, ?, ?> parent) {
        List<ArgumentCommandNode<S, ?>> nodes = toCommandNodes(defaultAction, parent);
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