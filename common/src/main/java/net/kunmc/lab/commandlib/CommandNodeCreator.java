package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.*;
import java.util.function.Predicate;
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
        builder.requires(x -> platformAdapter.hasPermission(command, x, permissionPrefix));

        List<Arguments<C>> argumentsList = command.argumentsList();
        ContextAction<C> helpAction = createSendHelpAction(command);

        if (argumentsList.isEmpty()) {
            CommandExecutor<S, C> executor = new CommandExecutor<>(platformAdapter,
                                                                   null,
                                                                   command.options(),
                                                                   command.prerequisite(),
                                                                   helpAction,
                                                                   command.preprocess(),
                                                                   command.contextAction(),
                                                                   command.uncaughtExceptionHandlers());
            builder.executes(executor);
            createOptionCommands(command.options(), () -> null, executor).forEach(builder::then);
            return builder.build();
        }

        argumentsList.stream()
                     .sorted((x, y) -> Integer.compare(y.size(),
                                                       x.size())) // Sort in descending order to handle variable-length arguments
                     .forEach(arguments -> {
                         CommandExecutor<S, C> executor = new CommandExecutor<>(platformAdapter,
                                                                                arguments,
                                                                                command.options(),
                                                                                command.prerequisite(),
                                                                                helpAction,
                                                                                command.preprocess(),
                                                                                command.contextAction(),
                                                                                command.uncaughtExceptionHandlers());
                         Supplier<CommandNode<S>> argumentNodeSupplier = () -> new ArgumentCommandNodeCreator<>(
                                 arguments).build(helpAction, command);
                         builder.then(argumentNodeSupplier.get())
                                .executes(executor);
                         createOptionCommands(command.options(), argumentNodeSupplier, executor).forEach(builder::then);
                     });

        return builder.build();
    }

    private List<CommandNode<S>> createOptionCommands(List<CommandOption<?, C>> options,
                                                      Supplier<CommandNode<S>> argumentNodeSupplier,
                                                      CommandExecutor<S, C> executor) {
        if (options.isEmpty()) {
            return List.of();
        }

        return createOptionCommands(options, new HashSet<>(), argumentNodeSupplier, executor);
    }

    private List<CommandNode<S>> createOptionCommands(List<CommandOption<?, C>> options,
                                                      Set<CommandOption<?, C>> selectedOptions,
                                                      Supplier<CommandNode<S>> argumentNodeSupplier,
                                                      CommandExecutor<S, C> executor) {
        List<CommandNode<S>> nodes = new ArrayList<>();

        for (OptionToken<C> optionToken : createOptionTokens(options, selectedOptions)) {
            List<CommandOption<?, C>> tokenOptions = optionToken.options();
            Set<CommandOption<?, C>> nextSelectedOptions = new HashSet<>(selectedOptions);
            nextSelectedOptions.addAll(tokenOptions);

            LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(optionToken.token());
            if (!isValueOptionToken(tokenOptions)) {
                builder.executes(executor);
            }

            CommandNode<S> argumentNode = argumentNodeSupplier.get();
            addArgumentAndOptionChildren(builder,
                                         tokenOptions,
                                         argumentNode,
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
                                              CommandNode<S> argumentNode,
                                              List<CommandOption<?, C>> options,
                                              Set<CommandOption<?, C>> nextSelectedOptions,
                                              Supplier<CommandNode<S>> argumentNodeSupplier,
                                              CommandExecutor<S, C> executor) {
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

    private List<CommandNode<S>> createAliasCommands(U source, CommandNode<S> redirectTarget) {
        return source.aliases()
                     .stream()
                     .map(s -> {
                         CommandNode<S> node = LiteralArgumentBuilder.<S>literal(s)
                                                                     .requires(x -> platformAdapter.hasPermission(source,
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

    private ContextAction<C> createSendHelpAction(U command) {
        return ctx -> {
            String border = ChatColorUtil.GRAY + "-".repeat(50);
            String padding = " ".repeat(2);

            String literalConcatName = ((Supplier<String>) () -> {
                LinkedList<U> commands = new LinkedList<>();
                commands.addFirst(command);
                U parent = command.parent();
                while (parent != null) {
                    commands.addFirst(parent);
                    parent = parent.parent();
                }

                List<String> names = commands.stream()
                                             .map(CommonCommand::name)
                                             .collect(Collectors.toList());
                names.set(0, ctx.getArg(0));
                return String.join(" ", names);
            }).get();

            ctx.sendMessage(border);

            if (!command.description()
                        .isEmpty()) {
                ctx.sendMessage(command.description());
            }
            ctx.sendMessage(ChatColorUtil.RED + "Usage:");

            List<U> permissibleChildren = command.children()
                                                 .stream()
                                                 .filter(x -> platformAdapter.hasPermission(x, ctx, permissionPrefix))
                                                 .collect(Collectors.toList());
            if (!permissibleChildren.isEmpty()) {
                ctx.sendMessage(ChatColorUtil.AQUA + padding + "/" + literalConcatName + concatOptionUsage(command));

                permissibleChildren.stream()
                                   .filter(x -> platformAdapter.hasPermission(x, ctx, permissionPrefix))
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

            List<String> concatenatedTagNames = command.argumentsList()
                                                       .stream()
                                                       .map(Arguments::concatTagNames)
                                                       .filter(Predicate.not(String::isEmpty))
                                                       .map(x -> padding + ChatColorUtil.AQUA + "/" + literalConcatName + concatOptionUsage(
                                                               command) + " " + x)
                                                       .collect(Collectors.toList());
            if (!permissibleChildren.isEmpty() && !concatenatedTagNames.isEmpty()) {
                ctx.sendMessage("");
            }
            concatenatedTagNames.forEach(ctx::sendMessage);

            List<String> optionDescriptions = command.options()
                                                     .stream()
                                                     .map(x -> formatOptionDescription(padding, x))
                                                     .collect(Collectors.toList());
            if (permissibleChildren.isEmpty() && concatenatedTagNames.isEmpty() && !optionDescriptions.isEmpty()) {
                ctx.sendMessage(ChatColorUtil.AQUA + padding + "/" + literalConcatName + concatOptionUsage(command));
            }
            if (!optionDescriptions.isEmpty()) {
                if (!permissibleChildren.isEmpty() || !concatenatedTagNames.isEmpty()) {
                    ctx.sendMessage("");
                }
                ctx.sendMessage(ChatColorUtil.RED + "Options:");
                optionDescriptions.forEach(ctx::sendMessage);
            }

            ctx.sendMessage(border);
        };
    }

    private String concatOptionUsage(U command) {
        if (command.options()
                   .isEmpty()) {
            return "";
        }

        return " " + ChatColorUtil.GRAY + "[" + ChatColorUtil.YELLOW + "options" + ChatColorUtil.GRAY + "]";
    }

    private String formatOptionDescription(String padding, CommandOption<?, C> option) {
        String s = ChatColorUtil.YELLOW + padding + formatOptionNames(option);
        if (option.description()
                  .isEmpty()) {
            return s;
        }

        return s + ChatColorUtil.WHITE + ": " + option.description();
    }

    private String formatOptionNames(CommandOption<?, C> option) {
        String valueTag = "";
        if (option.hasValue()) {
            valueTag = " " + ChatColorUtil.GRAY + "<" + ChatColorUtil.YELLOW + option.name() + ChatColorUtil.GRAY + ">";
        }

        String longName = "--" + option.name() + valueTag;
        if (option.shortName() == null) {
            return longName;
        }

        return "-" + option.shortName() + valueTag + ChatColorUtil.GRAY + ", " + ChatColorUtil.YELLOW + longName;
    }
}
