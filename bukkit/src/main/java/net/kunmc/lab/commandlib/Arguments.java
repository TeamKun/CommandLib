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

class Arguments {
    private final List<Argument<?>> argumentList;

    Arguments(List<Argument<?>> argumentList) {
        this.argumentList = argumentList;
    }

    void parse(List<Object> dstList, Map<String, Object> dstMap, CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        for (Argument<?> argument : argumentList) {
            try {
                Object parsedArg = argument.parse(ctx);
                dstList.add(parsedArg);
                dstMap.put(argument.name(), parsedArg);
            } catch (IllegalArgumentException ignored) {
                /*
                通常は発生しないが, argument追加時にContextActionを設定した場合やexecuteをOverrideした場合は
                com.mojang.brigadier.context.CommandContext#getArgument内で例外が発生する可能性があるため
                例外を無視している.
                 */
            }
        }
    }

    String generateHelpMessage(String literalConcatName) {
        if (argumentList.isEmpty()) {
            return "";
        }

        String msg = ChatColor.AQUA + "/" + literalConcatName + " ";
        msg += argumentList.stream()
                .map(Argument::generateHelpMessageTag)
                .collect(Collectors.joining(" "));

        return msg;
    }

    private RequiredArgumentBuilder<CommandListenerWrapper, ?> buildArgument(Argument<?> argument, Command parent) {
        RequiredArgumentBuilder<CommandListenerWrapper, ?> builder = RequiredArgumentBuilder.argument(argument.name(), argument.type());

        if (argument.suggestionAction() != null) {
            builder.suggests((ctx, sb) -> {
                List<Object> parsedArgList = new ArrayList<>();
                Map<String, Object> parsedArgMap = new HashMap<>();
                try {
                    parse(parsedArgList, parsedArgMap, ctx);
                } catch (IncorrectArgumentInputException ignored) {
                }

                SuggestionBuilder suggestionBuilder = new SuggestionBuilder(ctx, parsedArgList, parsedArgMap);
                argument.suggestionAction().accept(suggestionBuilder);
                suggestionBuilder.build().forEach(s -> {
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
                e.sendMessage(ctx.getSource().getBukkitSender());
                return 1;
            }
            return CommandLib.executeWithStackTrace(new net.kunmc.lab.commandlib.CommandContext(parent, ctx, parsedArgList, parsedArgMap), argument.contextAction());
        });

        return builder;
    }

    List<CommandNode<CommandListenerWrapper>> toCommandNodes(Command parent) {
        return argumentList.stream()
                .map(x -> buildArgument(x, parent))
                .map(RequiredArgumentBuilder::build)
                .collect(Collectors.toList());
    }
}
