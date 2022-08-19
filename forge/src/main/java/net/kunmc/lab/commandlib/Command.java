package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.kunmc.lab.commandlib.CommandLib.executeWithStackTrace;

public abstract class Command {
    private final String name;
    private int permissionLevel = 4;
    private Command parent = null;
    private final List<Command> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<Arguments> argumentsList = new ArrayList<>();

    public Command(@NotNull String name) {
        this.name = name;
    }

    public final void setPermissionLevel(int level) {
        this.permissionLevel = level;
    }

    public final void addChildren(@NotNull Command child, @NotNull Command... children) {
        addChildren(Lists.asList(child, children));
    }

    public final void addChildren(@NotNull List<Command> children) {
        this.children.addAll(children);

        for (Command child : children) {
            child.parent = this;
        }
    }

    public final void addAliases(@NotNull String alias, @NotNull String... aliases) {
        addAliases(Lists.asList(alias, aliases));
    }

    public final void addAliases(@NotNull List<String> aliases) {
        this.aliases.addAll(aliases);
    }

    public final void argument(@NotNull Consumer<ArgumentBuilder> buildArguments) {
        ArgumentBuilder builder = new ArgumentBuilder();
        buildArguments.accept(builder);
        argumentsList.add(new Arguments(builder.build()));
    }

    final List<LiteralCommandNode<CommandSource>> toCommandNodes() {
        List<LiteralCommandNode<CommandSource>> nodes = new ArrayList<>();

        LiteralCommandNode<CommandSource> node = toCommandNode();
        nodes.add(node);

        children.forEach(c -> {
            c.toCommandNodes().forEach(node::addChild);
        });

        nodes.addAll(createAliasCommands(node));

        return nodes;
    }

    private LiteralCommandNode<CommandSource> toCommandNode() {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal(name)
                .requires(cs -> cs.hasPermissionLevel(permissionLevel));
        if (argumentsList.isEmpty()) {
            builder.executes(ctx -> {
                return executeWithStackTrace(new CommandContext(this, ctx, new ArrayList<>(), new HashMap<>()), this::execute);
            });

            return builder.build();
        }

        for (Arguments arguments : argumentsList) {
            builder.executes(ctx -> {
                List<Object> parsedArgList = new ArrayList<>();
                Map<String, Object> parsedArgMap = new HashMap<>();
                try {
                    arguments.parse(parsedArgList, parsedArgMap, ctx);
                } catch (IncorrectArgumentInputException e) {
                    e.sendMessage(ctx.getSource());
                    return 1;
                }

                return executeWithStackTrace(new CommandContext(this, ctx, parsedArgList, parsedArgMap), this::execute);
            });

            List<ArgumentCommandNode<CommandSource, ?>> nodes = arguments.toCommandNodes(this);
            for (int i = 0; i < nodes.size() - 1; i++) {
                nodes.get(i).addChild(nodes.get(i + 1));
            }

            builder.then(nodes.get(0));
        }

        return builder.build();
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

    final void sendHelp(com.mojang.brigadier.context.CommandContext<CommandSource> ctx) {
        ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.GRAY + "--------------------------------------------------"), false);
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
            ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.AQUA + padding + "/" + literalConcatName), false);

            children.forEach(c -> {
                ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.YELLOW + padding + padding + c.name), false);
            });

            ctx.getSource().sendFeedback(new StringTextComponent(""), false);
        }

        for (Arguments arguments : argumentsList) {
            String msg = arguments.generateHelpMessage(literalConcatName);
            if (!msg.isEmpty()) {
                ctx.getSource().sendFeedback(new StringTextComponent(padding + msg), false);
            }
        }

        ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.GRAY + "--------------------------------------------------"), false);
    }

    protected void execute(@NotNull CommandContext ctx) {
        sendHelp(ctx.getHandle());
    }
}
