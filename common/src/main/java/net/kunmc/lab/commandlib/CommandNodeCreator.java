package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;
import net.kunmc.lab.commandlib.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class CommandNodeCreator<S, T, C extends AbstractCommandContext<S, T>, B extends AbstractArgumentBuilder<C, B>, U extends CommonCommand<C, B, U>> {
    private final PlatformAdapter<S, T, C, B, U> platformAdapter = PlatformAdapter.get();
    private final Collection<? extends U> commands;

    public CommandNodeCreator(Collection<? extends U> commands) {
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

        List<Arguments<C>> argumentsList = command.argumentsList();
        ContextAction<C> helpAction = createSendHelpAction(command);

        if (argumentsList.isEmpty()) {
            return builder.executes(context -> {
                              try {
                                  C ctx = platformAdapter.createCommandContext(context);

                                  if (!command.prerequisite()
                                              .test(ctx)) {
                                      return 0;
                                  }

                                  if (command.isContextActionUndefined()) {
                                      return helpAction.executeWithStackTrace(ctx);
                                  }

                                  if (!command.preprocess()
                                              .test(ctx)) {
                                      return 0;
                                  }

                                  return command.contextAction()
                                                .executeWithStackTrace(ctx);
                              } catch (Throwable e) {
                                  e.printStackTrace();
                                  throw e;
                              }
                          })
                          .build();
        }

        argumentsList.stream()
                     .sorted((x, y) -> Integer.compare(y.size(), x.size())) // 可変長引数のコマンドに対応させる
                     .forEach(arguments -> {
                         builder.then(new ArgumentCommandNodeCreator<>(arguments).build(helpAction, command))
                                .executes(context -> {
                                    try {
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

                                        if (!command.preprocess()
                                                    .test(ctx)) {
                                            return 0;
                                        }

                                        return command.contextAction()
                                                      .executeWithStackTrace(ctx);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                        throw e;
                                    }
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

    private ContextAction<C> createSendHelpAction(U command) {
        return ctx -> {
            String border = ChatColorUtil.GRAY + StringUtil.repeat("-", 50);
            String padding = StringUtil.repeat(" ", 2);

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

            List<String> concatenatedTagNames = command.argumentsList()
                                                       .stream()
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
