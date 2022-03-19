package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kunmc.lab.commandlib.annotation.NotNull;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Command {
    private final String name;
    private int permissionLevel = 4;
    private final List<Command> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final ArgumentBuilder argumentBuilder = new ArgumentBuilder();

    public Command(@NotNull String name) {
        this.name = name;
    }

    protected void setPermissionLevel(int level) {
        this.permissionLevel = level;
    }

    protected void addChildren(Command child, Command... children) {
        this.children.add(child);
        this.children.addAll(Arrays.asList(children));
    }

    protected void addAliases(String alias, String... aliases) {
        this.aliases.add(alias);
        this.aliases.addAll(Arrays.asList(aliases));
    }

    protected void argument(Consumer<ArgumentBuilder> buildArguments) {
        buildArguments.accept(argumentBuilder);
    }

    final List<LiteralCommandNode<CommandSource>> toCommandNodes() {
        List<LiteralCommandNode<CommandSource>> cmds = new ArrayList<>();
        List<Argument<?>> arguments = argumentBuilder.build();
        Function<com.mojang.brigadier.context.CommandContext<CommandSource>, List<Object>> argsParser = ctx -> arguments.stream()
                .map(a -> a.parse(ctx))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        LiteralArgumentBuilder<CommandSource> cmdBuilder = Commands.literal(name)
                .requires(cs -> cs.hasPermissionLevel(permissionLevel));

        if (arguments.isEmpty()) {
            cmdBuilder.executes(ctx -> {
                execute(new CommandContext(ctx.getSource(), ctx.getInput(), argsParser.apply(ctx)));
                return 1;
            });
        } else {
            List<RequiredArgumentBuilder<CommandSource, ?>> requiredArgumentBuilderList = arguments.stream()
                    .map(a -> a.toBuilder(argsParser))
                    .collect(Collectors.toList());
            requiredArgumentBuilderList.get(requiredArgumentBuilderList.size() - 1).executes(ctx -> {
                execute(new CommandContext(ctx.getSource(), ctx.getInput(), argsParser.apply(ctx)));
                return 1;
            });
            List<ArgumentCommandNode<CommandSource, ?>> argNodes = requiredArgumentBuilderList.stream()
                    .map(RequiredArgumentBuilder::build)
                    .collect(Collectors.toList());

            for (int i = 0; i < argNodes.size() - 1; i++) {
                argNodes.get(i).addChild(argNodes.get(i + 1));
            }

            cmdBuilder.then(argNodes.get(0));
        }

        LiteralCommandNode<CommandSource> cmd = cmdBuilder.build();
        cmds.add(cmd);

        children.forEach(c -> {
            c.toCommandNodes().forEach(cmd::addChild);
        });

        cmds.addAll(createAliasCommands(cmd));

        return cmds;
    }

    private List<LiteralCommandNode<CommandSource>> createAliasCommands(CommandNode<CommandSource> target) {
        return aliases.stream()
                .map(s -> Commands.literal(s)
                        .requires(cs -> cs.hasPermissionLevel(permissionLevel))
                        .redirect(target)
                        .build())
                .collect(Collectors.toList());
    }

    protected abstract void execute(CommandContext ctx);
}
