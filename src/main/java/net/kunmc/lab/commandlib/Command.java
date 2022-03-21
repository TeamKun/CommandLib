package net.kunmc.lab.commandlib;

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
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Command {
    private final String name;
    private int permissionLevel = 4;
    private final List<Command> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<ArgumentBuilder> argumentBuilderList = new ArrayList<>();

    public Command(@NotNull String name) {
        this.name = name;
    }

    protected void setPermissionLevel(int level) {
        this.permissionLevel = level;
    }

    protected void addChildren(@NotNull Command child, @NotNull Command... children) {
        this.children.add(child);
        this.children.addAll(Arrays.asList(children));
    }

    protected void addAliases(@NotNull String alias, @NotNull String... aliases) {
        this.aliases.add(alias);
        this.aliases.addAll(Arrays.asList(aliases));
    }

    protected void argument(@NotNull Consumer<ArgumentBuilder> buildArguments) {
        ArgumentBuilder builder = new ArgumentBuilder();
        buildArguments.accept(builder);
        argumentBuilderList.add(builder);
    }

    final List<LiteralCommandNode<CommandSource>> toCommandNodes() {
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
        if (argumentBuilderList.isEmpty()) {
            cmdBuilder.executes(ctx -> {
                return exec(new CommandContext(ctx.getSource(), ctx.getInput(), new ArrayList<>()));
            });
        }

        for (ArgumentBuilder argumentBuilder : argumentBuilderList) {
            List<Argument<?>> arguments = argumentBuilder.build();
            Function<com.mojang.brigadier.context.CommandContext<CommandSource>, List<Object>> argsParser = ctx -> {
                List<Object> parsedArgs = new ArrayList<>();

                for (Argument<?> argument : arguments) {
                    try {
                        parsedArgs.add(argument.parse(ctx));
                    } catch (Exception ignored) {
                        parsedArgs.add(null);
                    }
                }

                return parsedArgs;
            };

            cmdBuilder.executes(ctx -> sendHelp(ctx, arguments));

            List<RequiredArgumentBuilder<CommandSource, ?>> requiredArgumentBuilderList = arguments.stream()
                    .map(a -> a.toBuilder(argsParser))
                    .peek(a -> {
                        if (a.getCommand() == null) {
                            a.executes(ctx -> sendHelp(ctx, arguments));
                        }
                    })
                    .collect(Collectors.toList());
            requiredArgumentBuilderList.get(requiredArgumentBuilderList.size() - 1).executes(ctx -> {
                return exec(new CommandContext(ctx.getSource(), ctx.getInput(), argsParser.apply(ctx)));
            });
            List<ArgumentCommandNode<CommandSource, ?>> argNodes = requiredArgumentBuilderList.stream()
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

    private int sendHelp(com.mojang.brigadier.context.CommandContext<CommandSource> ctx, List<Argument<?>> arguments) {
        ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "Usage:"), false);
        String padding = "  ";

        if (!children.isEmpty()) {
            ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.BLUE + padding + "/" + name), false);

            children.forEach(c -> {
                ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.YELLOW + padding + padding + c.name), false);
            });

            ctx.getSource().sendFeedback(new StringTextComponent(""), false);
        }

        if (!arguments.isEmpty()) {
            String msg = TextFormatting.BLUE + padding + "/" + name + " ";
            msg += arguments.stream()
                    .map(a -> a.name)
                    .map(s -> String.format(TextFormatting.GRAY + "<" + TextFormatting.YELLOW + "%s" + TextFormatting.GRAY + ">", s))
                    .collect(Collectors.joining(" "));

            ctx.getSource().sendFeedback(new StringTextComponent(msg), false);
        }

        return 1;
    }

    private int exec(CommandContext ctx) {
        try {
            execute(ctx);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            ctx.sendFailure("An unexpected error occurred trying to execute that command.");
            ctx.sendFailure("Check the console for details.");
            return 0;
        }
    }

    protected abstract void execute(@NotNull CommandContext ctx);
}
