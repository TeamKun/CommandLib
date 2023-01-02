package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.stream.Collectors;

final class Arguments {
    private final List<Argument<?>> arguments;

    Arguments(List<Argument<?>> arguments) {
        this.arguments = arguments;
    }

    void parse(CommandContext ctx) throws IncorrectArgumentInputException {
        for (Argument<?> argument : arguments) {
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

        String msg = TextFormatting.AQUA + "/" + literalConcatName + " ";
        msg += arguments.stream()
                        .map(x -> String.format(TextFormatting.GRAY + "<" + TextFormatting.YELLOW + "%s" + TextFormatting.GRAY + ">",
                                                x.name()))
                        .collect(Collectors.joining(" "));

        return msg;
    }

    private RequiredArgumentBuilder<CommandSource, ?> buildArgument(Argument<?> argument, ContextAction defaultAction) {
        RequiredArgumentBuilder<CommandSource, ?> builder = RequiredArgumentBuilder.argument(argument.name(),
                                                                                             argument.type());

        if (argument.suggestionAction() != null) {
            builder.suggests((ctx, sb) -> {
                CommandContext context = new CommandContext(ctx);
                try {
                    parse(context);
                } catch (IncorrectArgumentInputException ignored) {
                }

                SuggestionBuilder suggestionBuilder = new SuggestionBuilder(context);
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
            CommandContext context = new CommandContext(ctx);
            try {
                parse(context);
            } catch (IncorrectArgumentInputException e) {
                e.sendMessage(context);
                return 1;
            }
            return CommandLib.executeWithStackTrace(context, argument.contextAction());
        });

        return builder;
    }

    int size() {
        return arguments.size();
    }

    private List<ArgumentCommandNode<CommandSource, ?>> toCommandNodes(ContextAction defaultAction) {
        return arguments.stream()
                        .map(x -> buildArgument(x, defaultAction))
                        .map(RequiredArgumentBuilder::build)
                        .collect(Collectors.toList());
    }

    ArgumentCommandNode<CommandSource, ?> build(ContextAction defaultAction) {
        List<ArgumentCommandNode<CommandSource, ?>> nodes = toCommandNodes(defaultAction);
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
