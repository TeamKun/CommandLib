package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

final class CommandNodeCreator<S, C extends AbstractCommandContext<S, ?>, A extends AbstractArguments<S, C>, B extends AbstractArgumentBuilder<C, A, B>, U extends CommonCommand<C, A, B, U>> {
    private final PlatformAdapter<S, C, A, B, U> platformAdapter;
    private final Collection<? extends U> commands;

    public CommandNodeCreator(PlatformAdapter<S, C, A, B, U> platformAdapter, Collection<? extends U> commands) {
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

        List<A> argumentsList = new ArrayList<>(command.argumentsList());
        if (argumentsList.isEmpty()) {
            builder.executes(ctx -> ContextAction.executeWithStackTrace(platformAdapter.createCommandContext(ctx),
                                                                        command::execute));

            return builder.build();
        }

        // 可変長引数のコマンドに対応させる
        argumentsList.sort((x, y) -> Integer.compare(y.size(), x.size()));

        for (A arguments : argumentsList) {
            // メソッドリファレンスにするとコンパイルが通らない
            builder.then(arguments.build(ctx -> command.sendHelp(ctx)));

            builder.executes(ctx -> {
                C context = platformAdapter.createCommandContext(ctx);
                try {
                    arguments.parse(context);
                } catch (IncorrectArgumentInputException e) {
                    e.sendMessage(context);
                    return 1;
                }

                return ContextAction.executeWithStackTrace(context, command::execute);
            });
        }

        return builder.build();
    }

    private List<CommandNode<S>> createAliasCommands(U source, CommandNode<S> redirectTarget) {
        return source.aliases()
                     .stream()
                     .map(s -> {
                         LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(s);
                         return builder.requires(x -> platformAdapter.hasPermission(source, x))
                                       .redirect(redirectTarget);
                     })
                     .peek(b -> {
                         if (!redirectTarget.getChildren()
                                            .isEmpty()) {
                             b.executes(ctx -> redirectTarget.getCommand()
                                                             .run(ctx));
                         }
                     })
                     .map(LiteralArgumentBuilder::build)
                     .collect(Collectors.toList());
    }
}
