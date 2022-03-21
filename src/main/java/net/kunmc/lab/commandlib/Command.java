package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.kunmc.lab.commandlib.CommandLib.executeWithStackTrace;

public abstract class Command {
    private final String name;
    private int permissionLevel = 4;
    private Command parent = null;
    private final List<Command> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<List<Argument<?>>> argumentsList = new ArrayList<>();

    public Command(@NotNull String name) {
        this.name = name;
    }

    public void setPermissionLevel(int level) {
        this.permissionLevel = level;
    }

    public void addChildren(@NotNull Command child, @NotNull Command... children) {
        addChildren(Lists.asList(child, children));
    }

    public void addChildren(@NotNull List<Command> children) {
        this.children.addAll(children);

        for (Command child : children) {
            child.parent = this;
        }
    }

    public void addAliases(@NotNull String alias, @NotNull String... aliases) {
        addAliases(Lists.asList(alias, aliases));
    }

    public void addAliases(@NotNull List<String> aliases) {
        this.aliases.addAll(aliases);
    }

    public void argument(@NotNull Consumer<ArgumentBuilder> buildArguments) {
        ArgumentBuilder builder = new ArgumentBuilder();
        buildArguments.accept(builder);
        argumentsList.add(builder.build());
    }

    public final List<LiteralCommandNode<CommandSource>> toCommandNodes() {
        List<LiteralCommandNode<CommandSource>> cmds = new ArrayList<>();

        LiteralCommandNode<CommandSource> cmd = toCommandNode();
        cmds.add(cmd);

        children.forEach(c -> {
            c.toCommandNodes().forEach(cmd::addChild);
        });

        cmds.addAll(createAliasCommands(cmd));

        return cmds;
    }

    private LiteralCommandNode<CommandSource> toCommandNode() {
        LiteralArgumentBuilder<CommandSource> cmdBuilder = Commands.literal(name)
                .requires(cs -> cs.hasPermissionLevel(permissionLevel));
        if (argumentsList.isEmpty()) {
            cmdBuilder.executes(ctx -> {
                return executeWithStackTrace(new CommandContext(this, ctx, new ArrayList<>()), this::execute);
            });

            return cmdBuilder.build();
        }

        for (List<Argument<?>> arguments : argumentsList) {
            Function<com.mojang.brigadier.context.CommandContext<CommandSource>, List<Object>> argsParser = ctx -> {
                List<Object> parsedArgs = new ArrayList<>();

                for (Argument<?> argument : arguments) {
                    try {
                        parsedArgs.add(argument.parse(ctx));
                    } catch (IllegalArgumentException ignored) {
                        // 通常は発生しないが, argument追加時にContextActionを設定した場合やexecuteをOverrideした場合は
                        // com.mojang.brigadier.context.CommandContext#getArgument内で例外が発生する可能性があるため
                        // 例外を無視している.
                    }
                }

                return parsedArgs;
            };

            cmdBuilder.executes(ctx -> {
                return executeWithStackTrace(new CommandContext(this, ctx, argsParser.apply(ctx)), this::execute);
            });

            List<ArgumentCommandNode<CommandSource, ?>> argNodes = arguments.stream()
                    .map(a -> a.toBuilder(this, argsParser))
                    .map(RequiredArgumentBuilder::build)
                    .collect(Collectors.toList());
            for (int i = 0; i < argNodes.size() - 1; i++) {
                argNodes.get(i).addChild(argNodes.get(i + 1));
            }

            cmdBuilder.then(argNodes.get(0));
        }

        return cmdBuilder.build();
    }

    private List<LiteralCommandNode<CommandSource>> createAliasCommands(CommandNode<CommandSource> target) {
        return aliases.stream()
                .map(s -> Commands.literal(s)
                        .requires(cs -> cs.hasPermissionLevel(permissionLevel))
                        .redirect(target))
                .peek(b -> {
                    if (!target.getChildren().isEmpty()) {
                        b.executes(ctx -> target.getCommand().run(ctx));
                    }
                })
                .map(LiteralArgumentBuilder::build)
                .collect(Collectors.toList());
    }

    public void sendHelp(com.mojang.brigadier.context.CommandContext<CommandSource> ctx) {
        ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "Usage:"), false);
        String padding = "  ";

        String literalConcatName = ((Supplier<String>) () -> {
            StringBuilder s = new StringBuilder(name);
            Command parent = this.parent;
            while (parent != null) {
                s.insert(0, parent.name + " ");
                parent = parent.parent;
            }

            return s.toString();
        }).get();

        if (!children.isEmpty()) {
            ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.BLUE + padding + "/" + literalConcatName), false);

            children.forEach(c -> {
                ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.YELLOW + padding + padding + c.name), false);
            });

            ctx.getSource().sendFeedback(new StringTextComponent(""), false);
        }

        for (List<Argument<?>> arguments : argumentsList) {
            if (arguments.isEmpty()) {
                continue;
            }

            String msg = TextFormatting.BLUE + padding + "/" + literalConcatName + " ";
            msg += arguments.stream()
                    .map(a -> a.name)
                    .map(s -> String.format(TextFormatting.GRAY + "<" + TextFormatting.YELLOW + "%s" + TextFormatting.GRAY + ">", s))
                    .collect(Collectors.joining(" "));

            ctx.getSource().sendFeedback(new StringTextComponent(msg), false);
        }
    }

    protected void execute(@NotNull CommandContext ctx) {
        sendHelp(ctx.getHandle());
    }
}
