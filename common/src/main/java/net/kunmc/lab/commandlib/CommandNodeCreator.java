package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class CommandNodeCreator<S, T, C extends AbstractCommandContext<S, T>, B extends AbstractArgumentBuilder<C, B>, U extends CommonCommand<C, B, U>> {
    private final PlatformAdapter<S, T, C, B, U> platformAdapter;
    private final Collection<? extends U> commands;

    public CommandNodeCreator(PlatformAdapter<S, T, C, B, U> platformAdapter, Collection<? extends U> commands) {
        this.platformAdapter = platformAdapter;
        this.commands = commands;
    }

    public List<CommandNode<S>> build() {
        return commands.stream()
                       .map(this::toCommandNodes)
                       .flatMap(Collection::stream)
                       .collect(Collectors.toList());
    }

    private List<CommandNode<S>> toCommandNodes(U command) {
        List<CommandNode<S>> nodes = new ArrayList<>();

        CommandNode<S> node = toCommandNode(command);
        nodes.add(node);

        command.children()
               .forEach(x -> {
                   toCommandNodes(x).forEach(node::addChild);
               });

        nodes.addAll(createAliasCommands(command, node));

        return nodes;
    }

    private CommandNode<S> toCommandNode(U command) {
        LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(command.name());
        List<Arguments<S, C>> argumentsList = command.argumentBuilderConsumers()
                                                     .stream()
                                                     .map(x -> {
                                                         B b = platformAdapter.createArgumentBuilder();
                                                         x.accept(b);
                                                         return new Arguments<>(b.build(), platformAdapter);
                                                     })
                                                     .collect(Collectors.toList());
        ContextAction<C> sendHelpAction = sendHelpAction(command, argumentsList);

        command.setContextActionIfAbsent(sendHelpAction);
        builder.requires(x -> platformAdapter.hasPermission(command, x));

        if (argumentsList.isEmpty()) {
            return builder.executes(ctx -> ContextAction.executeWithStackTrace(platformAdapter.createCommandContext(ctx),
                                                                               command.contextAction()))
                          .build();
        }

        argumentsList.stream()
                     .sorted((x, y) -> Integer.compare(y.size(), x.size())) // 可変長引数のコマンドに対応させる
                     .forEach(arguments -> {
                         builder.then(arguments.build(sendHelpAction, command))
                                .executes(ctx -> {
                                    C context = platformAdapter.createCommandContext(ctx);
                                    try {
                                        arguments.parse(context);
                                    } catch (IncorrectArgumentInputException e) {
                                        e.sendMessage(context);
                                        return 1;
                                    }

                                    return ContextAction.executeWithStackTrace(context, command.contextAction());
                                });
                     });

        return builder.build();
    }

    private List<CommandNode<S>> createAliasCommands(U source, CommandNode<S> redirectTarget) {
        return source.aliases()
                     .stream()
                     .map(s -> LiteralArgumentBuilder.<S>literal(s)
                                                     .requires(x -> platformAdapter.hasPermission(source, x))
                                                     .executes(x -> redirectTarget.getCommand()
                                                                                  .run(x))
                                                     .redirect(redirectTarget)
                                                     .build())
                     .collect(Collectors.toList());
    }

    private ContextAction<C> sendHelpAction(U command, List<Arguments<S, C>> argumentsList) {
        return ctx -> {
            String border = ChatColorUtil.GRAY + StringUtils.repeat("-", 50);
            String padding = StringUtils.repeat(" ", 2);
            String literalConcatName = ((Supplier<String>) () -> {
                StringBuilder s = new StringBuilder(command.name());
                U parent = command.parent();
                while (parent != null) {
                    s.insert(0, parent.name() + " ");
                    parent = parent.parent();
                }

                return s.toString();
            }).get();

            ctx.sendMessage(border);

            if (!command.description()
                        .isEmpty()) {
                ctx.sendMessage(command.description());
            }
            ctx.sendMessage(ChatColorUtil.RED + "Usage:");

            List<U> permissibleChildren = command.children()
                                                 .stream()
                                                 .filter(x -> platformAdapter.hasPermission(x, ctx))
                                                 .collect(Collectors.toList());
            if (!permissibleChildren.isEmpty()) {
                ctx.sendMessage(ChatColorUtil.AQUA + padding + "/" + literalConcatName);

                permissibleChildren.stream()
                                   .filter(x -> platformAdapter.hasPermission(x, ctx))
                                   .map(x -> {
                                       String s = ChatColorUtil.YELLOW + padding + padding + x.name();
                                       if (x.description()
                                            .isEmpty()) {
                                           return s;
                                       }
                                       return s + ChatColorUtil.WHITE + ": " + x.description();
                                   })
                                   .forEach(ctx::sendMessage);
            }

            List<String> concatenatedTagNames = argumentsList.stream()
                                                             .map(Arguments::concatTagNames)
                                                             .filter(x -> !x.isEmpty())
                                                             .map(x -> padding + ChatColorUtil.AQUA + "/" + literalConcatName + " " + x)
                                                             .collect(Collectors.toList());
            if (!permissibleChildren.isEmpty() && !concatenatedTagNames.isEmpty()) {
                ctx.sendMessage("");
            }
            concatenatedTagNames.forEach(ctx::sendMessage);

            ctx.sendMessage(border);
        };
    }
}
