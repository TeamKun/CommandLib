package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.List;
import java.util.stream.Collectors;

abstract class AbstractArguments<S, C extends AbstractCommandContext<S, ?>> {
    private final List<? extends CommonArgument<?, C>> arguments;

    AbstractArguments(List<? extends CommonArgument<?, C>> arguments) {
        this.arguments = arguments;
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

    String generateHelpMessage(String literalConcatName) {
        if (arguments.isEmpty()) {
            return "";
        }

        String msg = ChatColorUtil.AQUA + "/" + literalConcatName + " ";
        msg += arguments.stream()
                        .map(x -> String.format(ChatColorUtil.GRAY + "<" + ChatColorUtil.YELLOW + "%s" + ChatColorUtil.GRAY + ">",
                                                x.name()))
                        .collect(Collectors.joining(" "));

        return msg;
    }

    private RequiredArgumentBuilder<S, ?> buildArgument(CommonArgument<?, C> argument, ContextAction<C> defaultAction) {
        RequiredArgumentBuilder<S, ?> builder = RequiredArgumentBuilder.argument(argument.name(), argument.type());

        if (argument.suggestionAction() != null) {
            builder.suggests((ctx, sb) -> {
                C context = createCommandContext(ctx);
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
            C context = createCommandContext(ctx);
            try {
                parse(context);
            } catch (IncorrectArgumentInputException e) {
                e.sendMessage(context);
                return 1;
            }
            return ContextAction.executeWithStackTrace(context, argument.contextAction());
        });

        return builder;
    }

    int size() {
        return arguments.size();
    }

    private List<ArgumentCommandNode<S, ?>> toCommandNodes(ContextAction<C> defaultAction) {
        return arguments.stream()
                        .map(x -> buildArgument(x, defaultAction))
                        .map(RequiredArgumentBuilder::build)
                        .collect(Collectors.toList());
    }

    ArgumentCommandNode<S, ?> build(ContextAction<C> defaultAction) {
        List<ArgumentCommandNode<S, ?>> nodes = toCommandNodes(defaultAction);
        if (nodes.isEmpty()) {
            return null;
        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            nodes.get(i)
                 .addChild(nodes.get(i + 1));
        }

        return nodes.get(0);
    }

    abstract C createCommandContext(com.mojang.brigadier.context.CommandContext<S> ctx);
}
