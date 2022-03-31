package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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

    final List<CommandNode<CommandListenerWrapper>> toCommandNodes() {
        List<CommandNode<CommandListenerWrapper>> cmds = new ArrayList<>();

        CommandNode<CommandListenerWrapper> cmd = toCommandNode();
        cmds.add(cmd);

        children.forEach(c -> {
            c.toCommandNodes().forEach(cmd::addChild);
        });

        cmds.addAll(createAliasCommands(cmd));

        return cmds;
    }

    private CommandNode<CommandListenerWrapper> toCommandNode() {
        LiteralArgumentBuilder<CommandListenerWrapper> cmdBuilder = LiteralArgumentBuilder.literal(name);
        cmdBuilder.requires(cs -> cs.hasPermission(permissionLevel));
        if (argumentsList.isEmpty()) {
            cmdBuilder.executes(ctx -> {
                return executeWithStackTrace(new CommandContext(this, ctx, new ArrayList<>()), this::execute);
            });

            return cmdBuilder.build();
        }

        for (Arguments arguments : argumentsList) {
            cmdBuilder.executes(ctx -> {
                List<Object> parsedArguments = new ArrayList<>();
                try {
                    arguments.parse(parsedArguments, ctx);
                } catch (IncorrectArgumentInputException e) {
                    e.sendMessage(ctx.getSource().getBukkitSender());
                    return 1;
                }

                return executeWithStackTrace(new CommandContext(this, ctx, parsedArguments), this::execute);
            });

            List<CommandNode<CommandListenerWrapper>> argNodes = arguments.toCommandNodes(this);
            for (int i = 0; i < argNodes.size() - 1; i++) {
                argNodes.get(i).addChild(argNodes.get(i + 1));
            }

            cmdBuilder.then(argNodes.get(0));
        }

        return cmdBuilder.build();
    }

    private List<CommandNode<CommandListenerWrapper>> createAliasCommands(CommandNode<CommandListenerWrapper> target) {
        return aliases.stream()
                .map(s -> {
                    LiteralArgumentBuilder<CommandListenerWrapper> builder = LiteralArgumentBuilder.literal(s);
                    return builder.requires(cs -> cs.hasPermission(permissionLevel))
                            .redirect(target);
                })
                .peek(b -> {
                    if (!target.getChildren().isEmpty()) {
                        b.executes(ctx -> target.getCommand().run(ctx));
                    }
                })
                .map(LiteralArgumentBuilder::build)
                .collect(Collectors.toList());
    }

    final void sendHelp(com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> ctx) {
        CommandSender sender = ctx.getSource().getBukkitSender();

        sender.sendMessage(ChatColor.GRAY + "--------------------------------------------------");
        sender.sendMessage(ChatColor.RED + "Usage:");

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
            sender.sendMessage(ChatColor.AQUA + padding + "/" + literalConcatName);

            children.forEach(c -> {
                sender.sendMessage(ChatColor.YELLOW + padding + padding + c.name);
            });

            sender.sendMessage("");
        }

        for (Arguments arguments : argumentsList) {
            String msg = arguments.generateHelpMessage(literalConcatName);
            if (!msg.isEmpty()) {
                sender.sendMessage(padding + msg);
            }
        }

        sender.sendMessage(ChatColor.GRAY + "--------------------------------------------------");
    }

    protected void execute(@NotNull CommandContext ctx) {
        sendHelp(ctx.getHandle());
    }
}
