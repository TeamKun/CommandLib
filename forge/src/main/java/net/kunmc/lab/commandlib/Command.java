package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.fucntion.*;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.kunmc.lab.commandlib.CommandLib.executeWithStackTrace;

public abstract class Command {
    private final String name;
    private String description = "";
    private int permissionLevel = 4;
    private Command parent = null;
    private final List<Command> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<Arguments> argumentsList = new ArrayList<>();
    private ContextAction execute = this::sendHelp;

    public Command(@NotNull String name) {
        this.name = name;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public final void setPermissionLevel(int level) {
        this.permissionLevel = level;
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

    public final void execute(@NotNull ContextAction execute) {
        this.execute = execute;
    }

    final List<LiteralCommandNode<CommandSource>> toCommandNodes() {
        List<LiteralCommandNode<CommandSource>> nodes = new ArrayList<>();

        LiteralCommandNode<CommandSource> node = toCommandNode();
        nodes.add(node);

        children.forEach(c -> {
            c.toCommandNodes()
             .forEach(node::addChild);
        });

        nodes.addAll(createAliasCommands(node));

        return nodes;
    }

    private LiteralCommandNode<CommandSource> toCommandNode() {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal(name)
                                                                .requires(cs -> cs.hasPermissionLevel(permissionLevel));
        if (argumentsList.isEmpty()) {
            builder.executes(ctx -> {
                return executeWithStackTrace(new CommandContext(ctx), this::execute);
            });

            return builder.build();
        }

        // 可変長引数のコマンドに対応させる
        argumentsList.sort((x, y) -> Integer.compare(y.size(), x.size()));

        for (Arguments arguments : argumentsList) {
            builder.then(arguments.build(this::sendHelp));

            builder.executes(ctx -> {
                CommandContext context = new CommandContext(ctx);
                try {
                    arguments.parse(context);
                } catch (IncorrectArgumentInputException e) {
                    e.sendMessage(context);
                    return 1;
                }

                return executeWithStackTrace(context, this::execute);
            });
        }

        return builder.build();
    }

    private List<LiteralCommandNode<CommandSource>> createAliasCommands(CommandNode<CommandSource> target) {
        return aliases.stream()
                      .map(s -> Commands.literal(s)
                                        .requires(cs -> cs.hasPermissionLevel(permissionLevel))
                                        .redirect(target))
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
        String border = TextFormatting.GRAY + StringUtils.repeat("-", 50);
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

        if (!description.isEmpty()) {
            ctx.sendMessage(description);
        }
        ctx.sendMessage(TextFormatting.RED + "Usage:");

        if (!children.isEmpty()) {
            ctx.sendMessage(TextFormatting.AQUA + padding + "/" + literalConcatName);

            children.stream()
                    .map(x -> {
                        String s = TextFormatting.YELLOW + padding + padding + x.name;
                        if (x.description.isEmpty()) {
                            return s;
                        }
                        return s + TextFormatting.WHITE + ": " + x.description;
                    })
                    .forEach(ctx::sendMessage);
        }

        List<TextComponent> argumentsHelpMessages = argumentsList.stream()
                                                                 .map(x -> x.generateHelpMessage(literalConcatName))
                                                                 .filter(x -> !x.isEmpty())
                                                                 .map(x -> new StringTextComponent(padding + x))
                                                                 .collect(Collectors.toList());
        if (!children.isEmpty() && !argumentsHelpMessages.isEmpty()) {
            ctx.sendMessage("");
        }
        argumentsHelpMessages.forEach(ctx::sendMessage);

        ctx.sendMessage(border);
    }

    protected void execute(@NotNull CommandContext ctx) {
        execute.accept(ctx);
    }
}
