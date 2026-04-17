package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class CommandNodeCreator<S, T, C extends AbstractCommandContext<S, T>, B extends AbstractArgumentBuilder<C, B>, U extends CommonCommand<C, B, U>> {
    private final PlatformAdapter<S, T, C, B, U> platformAdapter = PlatformAdapter.get();
    private final Collection<? extends U> commands;
    private final String permissionPrefix;

    public CommandNodeCreator(Collection<? extends U> commands, String permissionPrefix) {
        this.commands = commands;
        this.permissionPrefix = permissionPrefix;
    }

    public List<LiteralCommandNode<S>> build() {
        return commands.stream()
                       .map(x -> toCommandNodes(x, List.of()))
                       .flatMap(Collection::stream)
                       .collect(Collectors.toList());
    }

    private List<LiteralCommandNode<S>> toCommandNodes(U command, List<Arguments<C>> inheritedArguments) {
        List<LiteralCommandNode<S>> nodes = new ArrayList<>();
        LiteralCommandNode<S> node = toCommandNode(command, inheritedArguments);
        nodes.add(node);

        command.children()
               .forEach(x -> {
                   // Normal command children do not consume new arguments, so they must keep the same inherited
                   // argument chain that was parsed before this command.
                   toCommandNodes(x, inheritedArguments).forEach(node::addChild);
               });

        nodes.addAll(createAliasCommands(command, node));

        return nodes;
    }

    private LiteralCommandNode<S> toCommandNode(U command, List<Arguments<C>> inheritedArguments) {
        LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(command.name());
        builder.requires(x -> platformAdapter.hasPermission(command, x, permissionPrefix));

        List<Arguments<C>> argumentsList = command.argumentsList();
        HelpMessageAction<S, T, C, B, U> helpAction = new HelpMessageAction<>(platformAdapter,
                                                                              command,
                                                                              permissionPrefix);

        if (argumentsList.isEmpty()) {
            CommandRunner<S, C> runner = new CommandRunner<>(platformAdapter,
                                                             inheritedArguments,
                                                             command.options(),
                                                             command.prerequisite(),
                                                             helpAction,
                                                             command.preprocess(),
                                                             command.executor(),
                                                             command.uncaughtExceptionHandlers());
            builder.executes(runner);
            createOptionCommands(command.options(), () -> null, runner).forEach(builder::then);
            return builder.build();
        }

        argumentsList.stream()
                     .sorted((x, y) -> Integer.compare(y.size(),
                                                       x.size())) // Sort in descending order to handle variable-length arguments
                     .forEach(arguments -> {
                         List<Arguments<C>> executorArguments = appendArgument(inheritedArguments, arguments);
                         CommandRunner<S, C> executor = new CommandRunner<>(platformAdapter,
                                                                            executorArguments,
                                                                            command.options(),
                                                                            command.prerequisite(),
                                                                            helpAction,
                                                                            command.preprocess(),
                                                                            command.executor(),
                                                                            command.uncaughtExceptionHandlers());
                         Supplier<ArgumentCommandNode<S, ?>> argumentNodeSupplier = () -> {
                             // Brigadier can attach literal nodes under an argument node. The executor still needs
                             // the parent arguments, so child commands inherit the complete argument chain here.
                             List<LiteralCommandNode<S>> childNodes = arguments.children()
                                                                               .stream()
                                                                               .map(x -> toCommandNodes(castCommand(x),
                                                                                                        executorArguments))
                                                                               .flatMap(Collection::stream)
                                                                               .collect(Collectors.toList());
                             return new ArgumentCommandNodeCreator<>(arguments, executorArguments).build(helpAction,
                                                                                                         command,
                                                                                                         childNodes);
                         };
                         builder.then(argumentNodeSupplier.get())
                                .executes(executor);
                         createOptionCommands(command.options(), argumentNodeSupplier, executor).forEach(builder::then);
                     });

        return builder.build();
    }

    private List<Arguments<C>> appendArgument(List<Arguments<C>> inheritedArguments, Arguments<C> arguments) {
        List<Arguments<C>> result = new ArrayList<>(inheritedArguments);
        result.add(arguments);
        return List.copyOf(result);
    }

    @SuppressWarnings("unchecked")
    private U castCommand(CommonCommand<C, ?, ?> command) {
        return (U) command;
    }

    private List<CommandNode<S>> createOptionCommands(List<CommandOption<?, C>> options,
                                                      Supplier<ArgumentCommandNode<S, ?>> argumentNodeSupplier,
                                                      CommandRunner<S, C> executor) {
        if (options.isEmpty()) {
            return List.of();
        }

        return createOptionCommands(options, new HashSet<>(), argumentNodeSupplier, executor);
    }

    private List<CommandNode<S>> createOptionCommands(List<CommandOption<?, C>> options,
                                                      Set<CommandOption<?, C>> selectedOptions,
                                                      Supplier<ArgumentCommandNode<S, ?>> argumentNodeSupplier,
                                                      CommandRunner<S, C> executor) {
        List<CommandNode<S>> nodes = new ArrayList<>();

        for (OptionToken<C> optionToken : createOptionTokens(options, selectedOptions)) {
            List<CommandOption<?, C>> tokenOptions = optionToken.options();
            Set<CommandOption<?, C>> nextSelectedOptions = new HashSet<>(selectedOptions);
            nextSelectedOptions.addAll(tokenOptions);

            LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(optionToken.token());
            if (!isValueOptionToken(tokenOptions)) {
                builder.executes(executor);
            }

            addArgumentAndOptionChildren(builder,
                                         tokenOptions,
                                         argumentNodeSupplier.get(),
                                         options,
                                         nextSelectedOptions,
                                         argumentNodeSupplier,
                                         executor);

            nodes.add(builder.build());
        }

        return nodes;
    }

    private void addArgumentAndOptionChildren(LiteralArgumentBuilder<S> builder,
                                              List<CommandOption<?, C>> tokenOptions,
                                              ArgumentCommandNode<S, ?> argumentNode,
                                              List<CommandOption<?, C>> options,
                                              Set<CommandOption<?, C>> nextSelectedOptions,
                                              Supplier<ArgumentCommandNode<S, ?>> argumentNodeSupplier,
                                              CommandRunner<S, C> executor) {
        if (isValueOptionToken(tokenOptions)) {
            CommandOption<?, C> option = tokenOptions.get(0);
            RequiredArgumentBuilder<S, ?> valueBuilder = RequiredArgumentBuilder.argument(option.internalName(),
                                                                                          option.type());
            valueBuilder.executes(executor);
            if (argumentNode != null) {
                valueBuilder.then(argumentNode);
            }
            createOptionCommands(options,
                                 nextSelectedOptions,
                                 argumentNodeSupplier,
                                 executor).forEach(valueBuilder::then);
            builder.then(valueBuilder);
            return;
        }

        if (argumentNode != null) {
            builder.then(argumentNode);
        }
        createOptionCommands(options, nextSelectedOptions, argumentNodeSupplier, executor).forEach(builder::then);
    }

    private boolean isValueOptionToken(List<CommandOption<?, C>> tokenOptions) {
        return tokenOptions.size() == 1 && tokenOptions.get(0)
                                                       .hasValue();
    }

    private List<OptionToken<C>> createOptionTokens(List<CommandOption<?, C>> options,
                                                    Set<CommandOption<?, C>> selectedOptions) {
        List<OptionToken<C>> optionTokens = new ArrayList<>();
        List<CommandOption<?, C>> remainingOptions = options.stream()
                                                            .filter(x -> !selectedOptions.contains(x))
                                                            .collect(Collectors.toList());

        for (CommandOption<?, C> option : remainingOptions) {
            optionTokens.add(new OptionToken<>("--" + option.name(), List.of(option)));
            if (option.shortName() != null) {
                optionTokens.add(new OptionToken<>("-" + option.shortName(), List.of(option)));
            }
        }

        List<CommandOption<?, C>> shortOptions = remainingOptions.stream()
                                                                 .filter(x -> x.shortName() != null && !x.hasValue())
                                                                 .collect(Collectors.toList());
        for (int size = 2; size <= shortOptions.size(); size++) {
            collectShortOptionPermutations(shortOptions, size, new ArrayList<>(), optionTokens);
        }

        return optionTokens;
    }

    private void collectShortOptionPermutations(List<CommandOption<?, C>> options,
                                                int size,
                                                List<CommandOption<?, C>> current,
                                                List<OptionToken<C>> dest) {
        if (current.size() == size) {
            dest.add(new OptionToken<>("-" + current.stream()
                                                    .map(CommandOption::shortName)
                                                    .map(String::valueOf)
                                                    .collect(Collectors.joining()), List.copyOf(current)));
            return;
        }

        for (CommandOption<?, C> option : options) {
            if (current.contains(option)) {
                continue;
            }
            current.add(option);
            collectShortOptionPermutations(options, size, current, dest);
            current.remove(current.size() - 1);
        }
    }

    private static class OptionToken<C extends AbstractCommandContext<?, ?>> {
        private final String token;
        private final List<CommandOption<?, C>> options;

        private OptionToken(String token, List<CommandOption<?, C>> options) {
            this.token = token;
            this.options = options;
        }

        private String token() {
            return token;
        }

        private List<CommandOption<?, C>> options() {
            return options;
        }
    }

    private List<LiteralCommandNode<S>> createAliasCommands(U source, LiteralCommandNode<S> redirectTarget) {
        return source.aliases()
                     .stream()
                     .map(s -> {
                         LiteralCommandNode<S> node = LiteralArgumentBuilder.<S>literal(s)
                                                                            .requires(x -> platformAdapter.hasPermission(
                                                                                    source,
                                                                                    x,
                                                                                    permissionPrefix))
                                                                            .executes(x -> redirectTarget.getCommand()
                                                                                                         .run(x))
                                                                            .build();
                         redirectTarget.getChildren()
                                       .forEach(node::addChild);
                         return node;
                     })
                     .collect(Collectors.toList());
    }

}
