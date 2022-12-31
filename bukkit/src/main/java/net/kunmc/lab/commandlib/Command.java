package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.fucntion.*;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.kunmc.lab.commandlib.CommandLib.executeWithStackTrace;

public abstract class Command {
    private final String name;
    private PermissionDefault defaultPermission = PermissionDefault.OP;
    private Command parent = null;
    private final List<Command> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<Arguments> argumentsList = new ArrayList<>();
    private Consumer<CommandContext> execute = this::sendHelp;

    public Command(@NotNull String name) {
        this.name = name;
    }

    public final void setPermission(PermissionDefault defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    public final void addChildren(@NotNull Command child, @NotNull Command... children) {
        addChildren(Lists.asList(child, children));
    }

    public final void addChildren(@NotNull Collection<? extends Command> children) {
        this.children.addAll(children);

        for (Command child : children) {
            child.parent = this;
        }
    }

    public final void addAliases(@NotNull String alias, @NotNull String... aliases) {
        addAliases(Lists.asList(alias, aliases));
    }

    public final void addAliases(@NotNull Collection<String> aliases) {
        this.aliases.addAll(aliases);
    }

    public final void argument(@NotNull Consumer<ArgumentBuilder> buildArguments) {
        ArgumentBuilder builder = new ArgumentBuilder();
        buildArguments.accept(builder);
        argumentsList.add(new Arguments(builder.build()));
    }

    public final <T> void argument(@NotNull Argument<T> argument, @NotNull BiConsumer<T, CommandContext> execute) {
        argument(builder -> {
            builder.customArgument(argument)
                   .execute(ctx -> {
                       execute.accept(argument.cast(ctx.getParsedArg(argument.name)), ctx);
                   });
        });
    }

    public final <T1, T2> void argument(@NotNull Argument<T1> argument1,
                                        @NotNull Argument<T2> argument2,
                                        @NotNull TriConsumer<T1, T2, CommandContext> execute) {
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

    public final <T1, T2, T3> void argument(@NotNull Argument<T1> argument1,
                                            @NotNull Argument<T2> argument2,
                                            @NotNull Argument<T3> argument3,
                                            @NotNull TetraConsumer<T1, T2, T3, CommandContext> execute) {
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

    public final <T1, T2, T3, T4> void argument(@NotNull Argument<T1> argument1,
                                                @NotNull Argument<T2> argument2,
                                                @NotNull Argument<T3> argument3,
                                                @NotNull Argument<T4> argument4,
                                                @NotNull QuintConsumer<T1, T2, T3, T4, CommandContext> execute) {
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

    public final <T1, T2, T3, T4, T5> void argument(@NotNull Argument<T1> argument1,
                                                    @NotNull Argument<T2> argument2,
                                                    @NotNull Argument<T3> argument3,
                                                    @NotNull Argument<T4> argument4,
                                                    @NotNull Argument<T5> argument5,
                                                    @NotNull HexaConsumer<T1, T2, T3, T4, T5, CommandContext> execute) {
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

    public final <T1, T2, T3, T4, T5, T6> void argument(@NotNull Argument<T1> argument1,
                                                        @NotNull Argument<T2> argument2,
                                                        @NotNull Argument<T3> argument3,
                                                        @NotNull Argument<T4> argument4,
                                                        @NotNull Argument<T5> argument5,
                                                        @NotNull Argument<T6> argument6,
                                                        @NotNull HeptConsumer<T1, T2, T3, T4, T5, T6, CommandContext> execute) {
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

    public final <T1, T2, T3, T4, T5, T6, T7> void argument(@NotNull Argument<T1> argument1,
                                                            @NotNull Argument<T2> argument2,
                                                            @NotNull Argument<T3> argument3,
                                                            @NotNull Argument<T4> argument4,
                                                            @NotNull Argument<T5> argument5,
                                                            @NotNull Argument<T6> argument6,
                                                            @NotNull Argument<T7> argument7,
                                                            @NotNull OctoConsumer<T1, T2, T3, T4, T5, T6, T7, CommandContext> execute) {
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

    public final void execute(@NotNull Consumer<CommandContext> execute) {
        this.execute = execute;
    }

    public final String permissionName() {
        return "minecraft.command." + permissionNameWithoutPrefix();
    }

    List<Permission> permissions() {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(new Permission(permissionName(), defaultPermission));
        permissions.addAll(children.stream()
                                   .map(Command::permissions)
                                   .reduce(new ArrayList<>(), (x, y) -> {
                                       x.addAll(y);
                                       return x;
                                   }));

        return permissions;
    }

    private String permissionNameWithoutPrefix() {
        if (parent == null) {
            return name;
        }
        return parent.permissionNameWithoutPrefix() + "." + name;
    }

    final List<CommandNode<CommandListenerWrapper>> toCommandNodes() {
        List<CommandNode<CommandListenerWrapper>> nodes = new ArrayList<>();

        CommandNode<CommandListenerWrapper> node = toCommandNode();
        nodes.add(node);

        children.forEach(x -> {
            x.toCommandNodes()
             .forEach(node::addChild);
        });

        nodes.addAll(createAliasCommands(node));

        return nodes;
    }

    private CommandNode<CommandListenerWrapper> toCommandNode() {
        LiteralArgumentBuilder<CommandListenerWrapper> builder = LiteralArgumentBuilder.literal(name);
        builder.requires(cs -> cs.getBukkitSender()
                                 .hasPermission(permissionName()));
        if (argumentsList.isEmpty()) {
            builder.executes(ctx -> {
                return executeWithStackTrace(new CommandContext(this, ctx, new ArrayList<>(), new HashMap<>()),
                                             this::execute);
            });

            return builder.build();
        }

        // 可変長引数のコマンドに対応させる
        argumentsList.sort((x, y) -> Integer.compare(y.size(), x.size()));

        for (Arguments arguments : argumentsList) {
            builder.executes(ctx -> {
                List<Object> parsedArgList = new ArrayList<>();
                Map<String, Object> parsedArgMap = new HashMap<>();
                try {
                    arguments.parse(parsedArgList, parsedArgMap, ctx);
                } catch (IncorrectArgumentInputException e) {
                    e.sendMessage(ctx.getSource()
                                     .getBukkitSender());
                    return 1;
                }

                return executeWithStackTrace(new CommandContext(this, ctx, parsedArgList, parsedArgMap), this::execute);
            });

            List<CommandNode<CommandListenerWrapper>> nodes = arguments.toCommandNodes(this);
            for (int i = 0; i < nodes.size() - 1; i++) {
                nodes.get(i)
                     .addChild(nodes.get(i + 1));
            }

            builder.then(nodes.get(0));
        }

        return builder.build();
    }

    private List<CommandNode<CommandListenerWrapper>> createAliasCommands(CommandNode<CommandListenerWrapper> target) {
        return aliases.stream()
                      .map(s -> {
                          LiteralArgumentBuilder<CommandListenerWrapper> builder = LiteralArgumentBuilder.literal(s);
                          return builder.requires(x -> x.getBukkitSender()
                                                        .hasPermission(permissionName()))
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

    final void sendHelp(CommandContext ctx) {
        String border = ChatColor.GRAY + StringUtils.repeat("-", 50);
        String padding = StringUtils.repeat(" ", 2);
        String literalConcatName = ((Supplier<String>) () -> {
            StringBuilder s = new StringBuilder(name);
            Command parent = this.parent;
            while (parent != null) {
                s.insert(0, parent.name + " ");
                parent = parent.parent;
            }

            return s.toString();
        }).get();

        ctx.sendMessage(border);
        ctx.sendMessage(ChatColor.RED + "Usage:");

        if (!children.isEmpty()) {
            ctx.sendMessage(ChatColor.AQUA + padding + "/" + literalConcatName);

            children.stream()
                    .filter(x -> ctx.getSender()
                                    .hasPermission(x.permissionName()))
                    .forEach(x -> {
                        ctx.sendMessage(ChatColor.YELLOW + padding + padding + x.name);
                    });

            ctx.sendMessage("");
        }

        for (Arguments arguments : argumentsList) {
            String msg = arguments.generateHelpMessage(literalConcatName);
            if (!msg.isEmpty()) {
                ctx.sendMessage(padding + msg);
            }
        }

        ctx.sendMessage(border);
    }

    protected void execute(@NotNull CommandContext ctx) {
        execute.accept(ctx);
    }
}
