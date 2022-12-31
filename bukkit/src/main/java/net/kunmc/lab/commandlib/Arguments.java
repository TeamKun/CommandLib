package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class Arguments {
    private final List<Argument<?>> argumentList;

    Arguments(List<Argument<?>> argumentList) {
        this.argumentList = argumentList;
    }

    void parse(List<Object> dstList,
               Map<String, Object> dstMap,
               CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        for (Argument<?> argument : argumentList) {
            try {
                Object parsedArg = argument.parseInternal(ctx);
                dstList.add(parsedArg);
                dstMap.put(argument.name(), parsedArg);
            } catch (IllegalArgumentException ignored) {
                // 補完時に入力中の引数で例外が発生するため握りつぶす
            }
        }
    }

    String generateHelpMessage(String literalConcatName) {
        if (argumentList.isEmpty()) {
            return "";
        }

        String msg = ChatColor.AQUA + "/" + literalConcatName + " ";
        msg += argumentList.stream()
                           .map(x -> String.format(ChatColor.GRAY + "<" + ChatColor.YELLOW + "%s" + ChatColor.GRAY + ">",
                                                   x.name()))
                           .collect(Collectors.joining(" "));

        return msg;
    }

    private RequiredArgumentBuilder<CommandListenerWrapper, ?> buildArgument(Argument<?> argument, Command parent) {
        RequiredArgumentBuilder<CommandListenerWrapper, ?> builder = RequiredArgumentBuilder.argument(argument.name(),
                                                                                                      argument.type());

        if (argument.suggestionAction() != null) {
            builder.suggests((ctx, sb) -> {
                List<Object> parsedArgList = new ArrayList<>();
                Map<String, Object> parsedArgMap = new HashMap<>();
                try {
                    parse(parsedArgList, parsedArgMap, ctx);
                } catch (IncorrectArgumentInputException ignored) {
                }

                SuggestionBuilder suggestionBuilder = new SuggestionBuilder(ctx, parsedArgList, parsedArgMap);
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
            argument.setContextAction(parent::execute);
        }

        builder.executes(ctx -> {
            List<Object> parsedArgList = new ArrayList<>();
            Map<String, Object> parsedArgMap = new HashMap<>();
            try {
                parse(parsedArgList, parsedArgMap, ctx);
            } catch (IncorrectArgumentInputException e) {
                e.sendMessage(ctx.getSource()
                                 .getBukkitSender());
                return 1;
            }
            return CommandLib.executeWithStackTrace(new net.kunmc.lab.commandlib.CommandContext(parent,
                                                                                                ctx,
                                                                                                parsedArgList,
                                                                                                parsedArgMap),
                                                    argument.contextAction());
        });

        return builder;
    }

    int size() {
        return argumentList.size();
    }

    List<CommandNode<CommandListenerWrapper>> toCommandNodes(Command parent) {
        return argumentList.stream()
                           .map(x -> buildArgument(x, parent))
                           .map(RequiredArgumentBuilder::build)
                           .collect(Collectors.toList());
    }
}
