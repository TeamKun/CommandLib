package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;
import net.kunmc.lab.commandlib.util.fucntion.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class CommonCommand<S, C extends AbstractCommandContext<S, ?>, A extends AbstractArguments<S, C>, B extends AbstractArgumentBuilder<C, A, B>, T extends CommonCommand<S, C, A, B, T>> {
    private final String name;
    private String description = "";
    private T parent = null;
    private final List<T> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<A> argumentsList = new ArrayList<>();
    private ContextAction<C> execute = this::sendHelp;

    public CommonCommand(@NotNull String name) {
        this.name = name;
    }

    public final String name() {
        return name;
    }

    public final String description() {
        return description;
    }

    public final void setDescription(@NotNull String description) {
        this.description = description;
    }

    public final void addChildren(@NotNull T child, @NotNull T... children) {
        addChildren(Lists.asList(child, children));
    }

    public final void addChildren(@NotNull Collection<? extends T> children) {
        this.children.addAll(children);

        for (T child : children) {
            child.setParent((T) this);
        }
    }

    public final void addAliases(@NotNull String alias, @NotNull String... aliases) {
        addAliases(Lists.asList(alias, aliases));
    }

    public final void addAliases(@NotNull Collection<String> aliases) {
        this.aliases.addAll(aliases);
    }

    public final void argument(@NotNull Consumer<B> buildArguments) {
        B builder = createArgumentBuilder();
        buildArguments.accept(builder);
        argumentsList.add(builder.build());
    }

    public final <T1> void argument(@NotNull CommonArgument<T1, C> argument, @NotNull BiConsumer<T1, C> execute) {
        argument(builder -> {
            builder.customArgument(argument)
                   .execute(ctx -> {
                       execute.accept(argument.cast(ctx.getParsedArg(argument.name)), ctx);
                   });
        });
    }

    public final <T1, T2> void argument(@NotNull CommonArgument<T1, C> argument1,
                                        @NotNull CommonArgument<T2, C> argument2,
                                        @NotNull TriConsumer<T1, T2, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name)),
                                      argument2.cast(ctx.getParsedArg(argument2.name)),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3> void argument(@NotNull CommonArgument<T1, C> argument1,
                                            @NotNull CommonArgument<T2, C> argument2,
                                            @NotNull CommonArgument<T3, C> argument3,
                                            @NotNull TetraConsumer<T1, T2, T3, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name)),
                                      argument2.cast(ctx.getParsedArg(argument2.name)),
                                      argument3.cast(ctx.getParsedArg(argument3.name)),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3, T4> void argument(@NotNull CommonArgument<T1, C> argument1,
                                                @NotNull CommonArgument<T2, C> argument2,
                                                @NotNull CommonArgument<T3, C> argument3,
                                                @NotNull CommonArgument<T4, C> argument4,
                                                @NotNull QuintConsumer<T1, T2, T3, T4, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .customArgument(argument4)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name)),
                                      argument2.cast(ctx.getParsedArg(argument2.name)),
                                      argument3.cast(ctx.getParsedArg(argument3.name)),
                                      argument4.cast(ctx.getParsedArg(argument4.name)),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3, T4, T5> void argument(@NotNull CommonArgument<T1, C> argument1,
                                                    @NotNull CommonArgument<T2, C> argument2,
                                                    @NotNull CommonArgument<T3, C> argument3,
                                                    @NotNull CommonArgument<T4, C> argument4,
                                                    @NotNull CommonArgument<T5, C> argument5,
                                                    @NotNull HexaConsumer<T1, T2, T3, T4, T5, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .customArgument(argument4)
                   .customArgument(argument5)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name)),
                                      argument2.cast(ctx.getParsedArg(argument2.name)),
                                      argument3.cast(ctx.getParsedArg(argument3.name)),
                                      argument4.cast(ctx.getParsedArg(argument4.name)),
                                      argument5.cast(ctx.getParsedArg(argument5.name)),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3, T4, T5, T6> void argument(@NotNull CommonArgument<T1, C> argument1,
                                                        @NotNull CommonArgument<T2, C> argument2,
                                                        @NotNull CommonArgument<T3, C> argument3,
                                                        @NotNull CommonArgument<T4, C> argument4,
                                                        @NotNull CommonArgument<T5, C> argument5,
                                                        @NotNull CommonArgument<T6, C> argument6,
                                                        @NotNull HeptConsumer<T1, T2, T3, T4, T5, T6, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .customArgument(argument4)
                   .customArgument(argument5)
                   .customArgument(argument6)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name)),
                                      argument2.cast(ctx.getParsedArg(argument2.name)),
                                      argument3.cast(ctx.getParsedArg(argument3.name)),
                                      argument4.cast(ctx.getParsedArg(argument4.name)),
                                      argument5.cast(ctx.getParsedArg(argument5.name)),
                                      argument6.cast(ctx.getParsedArg(argument6.name)),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3, T4, T5, T6, T7> void argument(@NotNull CommonArgument<T1, C> argument1,
                                                            @NotNull CommonArgument<T2, C> argument2,
                                                            @NotNull CommonArgument<T3, C> argument3,
                                                            @NotNull CommonArgument<T4, C> argument4,
                                                            @NotNull CommonArgument<T5, C> argument5,
                                                            @NotNull CommonArgument<T6, C> argument6,
                                                            @NotNull CommonArgument<T7, C> argument7,
                                                            @NotNull OctoConsumer<T1, T2, T3, T4, T5, T6, T7, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .customArgument(argument4)
                   .customArgument(argument5)
                   .customArgument(argument6)
                   .customArgument(argument7)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name)),
                                      argument2.cast(ctx.getParsedArg(argument2.name)),
                                      argument3.cast(ctx.getParsedArg(argument3.name)),
                                      argument4.cast(ctx.getParsedArg(argument4.name)),
                                      argument5.cast(ctx.getParsedArg(argument5.name)),
                                      argument6.cast(ctx.getParsedArg(argument6.name)),
                                      argument7.cast(ctx.getParsedArg(argument7.name)),
                                      ctx);
                   });
        });
    }

    public final void execute(@NotNull ContextAction<C> execute) {
        this.execute = execute;
    }

    final T parent() {
        return parent;
    }

    final void setParent(T parent) {
        this.parent = parent;
    }

    final List<T> children() {
        return Collections.unmodifiableList(children);
    }

    abstract boolean hasPermission(S s);

    abstract boolean hasPermission(C ctx);

    final List<CommandNode<S>> toCommandNodes() {
        List<CommandNode<S>> nodes = new ArrayList<>();

        CommandNode<S> node = toCommandNode();
        nodes.add(node);

        children.forEach(x -> {
            x.toCommandNodes()
             .forEach(node::addChild);
        });

        nodes.addAll(createAliasCommands(node));

        return nodes;
    }

    private CommandNode<S> toCommandNode() {
        LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(name);
        builder.requires(this::hasPermission);
        if (argumentsList.isEmpty()) {
            builder.executes(ctx -> ContextAction.executeWithStackTrace(createCommandContext(ctx), this::execute));

            return builder.build();
        }

        // 可変長引数のコマンドに対応させる
        argumentsList.sort((x, y) -> Integer.compare(y.size(), x.size()));

        for (AbstractArguments<S, C> arguments : argumentsList) {
            builder.then(arguments.build(this::sendHelp));

            builder.executes(ctx -> {
                C context = createCommandContext(ctx);
                try {
                    arguments.parse(context);
                } catch (IncorrectArgumentInputException e) {
                    e.sendMessage(context);
                    return 1;
                }

                return ContextAction.executeWithStackTrace(context, this::execute);
            });
        }

        return builder.build();
    }

    private List<CommandNode<S>> createAliasCommands(CommandNode<S> target) {
        return aliases.stream()
                      .map(s -> {
                          LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(s);
                          return builder.requires(this::hasPermission)
                                        .redirect(target);
                      })
                      .peek(b -> {
                          if (!target.getChildren()
                                     .isEmpty()) {
                              b.executes(ctx -> target.getCommand()
                                                      .run(ctx));
                          }
                      })
                      .map(LiteralArgumentBuilder::build)
                      .collect(Collectors.toList());
    }

    final void sendHelp(C ctx) {
        String border = ChatColorUtil.GRAY + StringUtils.repeat("-", 50);
        String padding = StringUtils.repeat(" ", 2);
        String literalConcatName = ((Supplier<String>) () -> {
            StringBuilder s = new StringBuilder(name);
            T parent = parent();
            while (parent != null) {
                s.insert(0, parent.name() + " ");
                parent = parent.parent();
            }

            return s.toString();
        }).get();

        ctx.sendMessage(border);

        if (!description.isEmpty()) {
            ctx.sendMessage(description);
        }
        ctx.sendMessage(ChatColorUtil.RED + "Usage:");

        List<T> permissibleChildren = children.stream()
                                              .filter(x -> x.hasPermission(ctx))
                                              .collect(Collectors.toList());
        if (!permissibleChildren.isEmpty()) {
            ctx.sendMessage(ChatColorUtil.AQUA + padding + "/" + literalConcatName);

            permissibleChildren.stream()
                               .filter(x -> x.hasPermission(ctx))
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

        List<String> argumentsHelpMessages = argumentsList.stream()
                                                          .map(x -> x.generateHelpMessage(literalConcatName))
                                                          .filter(x -> !x.isEmpty())
                                                          .map(x -> padding + x)
                                                          .collect(Collectors.toList());
        if (!permissibleChildren.isEmpty() && !argumentsHelpMessages.isEmpty()) {
            ctx.sendMessage("");
        }
        argumentsHelpMessages.forEach(ctx::sendMessage);

        ctx.sendMessage(border);
    }

    protected void execute(@NotNull C ctx) {
        execute.accept(ctx);
    }

    abstract B createArgumentBuilder();

    abstract C createCommandContext(com.mojang.brigadier.context.CommandContext<S> ctx);
}
