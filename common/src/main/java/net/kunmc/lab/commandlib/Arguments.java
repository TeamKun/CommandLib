package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.List;
import java.util.stream.Collectors;

final class Arguments<S, T, C extends AbstractCommandContext<S, T>> {
    private final List<? extends CommonArgument<?, C>> arguments;
    private final PlatformAdapter<S, ?, C, ?, ?> platformAdapter = PlatformAdapter.get();

    Arguments(List<? extends CommonArgument<?, C>> arguments) {
        this.arguments = arguments;
    }

    void parse(C ctx) throws IncorrectArgumentInputException {
        long count = ctx.getHandle()
                        .getNodes()
                        .stream()
                        .map(ParsedCommandNode::getNode)
                        .filter(x -> x instanceof ArgumentCommandNode)
                        .count();

        for (int i = 0; i < count; i++) {
            CommonArgument<?, C> argument = arguments.get(i);
            Object parsedArg = argument.parse(ctx);
            ctx.addParsedArgument(argument.name(), parsedArg);
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
                                                        ContextAction<C> helpAction,
                                                        CommonCommand<C, ?, ?> parent) {
        RequiredArgumentBuilder<S, ?> builder = RequiredArgumentBuilder.argument(argument.name(), argument.type());

        if (argument.suggestionAction() != null) {
            builder.suggests((context, sb) -> {
                try {
                    C ctx = platformAdapter.createCommandContext(context);
                    try {
                        parse(ctx);
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

        builder.executes(context -> {
            try {
                C ctx = platformAdapter.createCommandContext(context);

                try {
                    parse(ctx);
                } catch (IncorrectArgumentInputException e) {
                    e.sendMessage(ctx);
                    return 1;
                }

                if (!parent.prerequisite()
                           .test(ctx)) {
                    return 1;
                }

                if (argument.isContextActionUndefined()) {
                    return helpAction.executeWithStackTrace(ctx);
                }

                if (!parent.preprocess()
                           .test(ctx)) {
                    return 1;
                }

                return argument.contextAction()
                               .executeWithStackTrace(ctx);
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
            }
        });

        return builder;
    }

    int size() {
        return arguments.size();
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
