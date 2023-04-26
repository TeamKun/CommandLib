package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
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
        builder.requires(x -> platformAdapter.hasPermission(command, x));

        List<Arguments<S, C>> argumentsList = command.argumentBuilderConsumers()
                                                     .stream()
                                                     .map(x -> {
                                                         B b = platformAdapter.createArgumentBuilder();
                                                         x.accept(b);
                                                         return new Arguments<>(b.build(), platformAdapter);
                                                     })
                                                     .collect(Collectors.toList());
        ContextAction<C> helpAction = createSendHelpAction(command, argumentsList);

        if (argumentsList.isEmpty()) {
            return builder.executes(context -> {
                              C ctx = platformAdapter.createCommandContext(context);

                              if (!command.prerequisite()
                                          .test(ctx)) {
                                  return 0;
                              }

                              if (command.isContextActionUndefined()) {
                                  return helpAction.executeWithStackTrace(ctx);
                              }

                              return command.contextAction()
                                            .executeWithStackTrace(ctx);
                          })
                          .build();
        }

        argumentsList.stream()
                     .sorted((x, y) -> Integer.compare(y.size(), x.size())) // 可変長引数のコマンドに対応させる
                     .forEach(arguments -> {
                         builder.then(arguments.build(helpAction, command))
                                .executes(context -> {
                                    C ctx = platformAdapter.createCommandContext(context);

                                    try {
                                        arguments.parse(ctx);
                                    } catch (IncorrectArgumentInputException e) {
                                        e.sendMessage(ctx);
                                        return 1;
                                    }

                                    if (!command.prerequisite()
                                                .test(ctx)) {
                                        return 0;
                                    }

                                    if (command.isContextActionUndefined()) {
                                        return helpAction.executeWithStackTrace(ctx);
                                    }

                                    return command.contextAction()
                                                  .executeWithStackTrace(ctx);
                                });
                     });

        return builder.build();
    }

    private List<CommandNode<S>> createAliasCommands(U source, CommandNode<S> redirectTarget) {
        return source.aliases()
                     .stream()
                     .map(s -> {
                         CommandNode<S> node = LiteralArgumentBuilder.<S>literal(s)
                                                                     .requires(x -> platformAdapter.hasPermission(source,
                                                                                                                  x))
                                                                     .executes(x -> redirectTarget.getCommand()
                                                                                                  .run(x))
                                                                     .build();
                         redirectTarget.getChildren()
                                       .forEach(node::addChild);
                         return node;
                     })
                     .collect(Collectors.toList());
    }

    private ContextAction<C> createSendHelpAction(U command, List<Arguments<S, C>> argumentsList) {
        return ctx -> {
            String border = ChatColorUtil.GRAY + StringUtils.repeat("-", 50);
            String padding = StringUtils.repeat(" ", 2);

            String literalConcatName = ((Supplier<String>) () -> {
                LinkedList<U> commands = new LinkedList<>();
                commands.addFirst(command);
                U parent = command.parent();
                while (parent != null) {
                    commands.addFirst(parent);
                    parent = parent.parent();
                }

                List<String> names = commands.stream()
                                             .map(CommonCommand::name)
                                             .collect(Collectors.toList());
                names.set(0, ctx.getArg(0));
                return String.join(" ", names);
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
