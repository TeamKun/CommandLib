package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.branch.*;
import net.kunmc.lab.commandlib.util.UncaughtExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class CommonCommand<C extends AbstractCommandContext<?, ?>, B extends AbstractArgumentBuilder<C, B>, T extends CommonCommand<C, B, T>> {
    private final String name;
    private String description = "";
    private T parent = null;
    private Arguments<C> parentArguments = null;
    private boolean inheritParentPrerequisite = true;
    private boolean inheritParentPreprocess = true;
    private final List<T> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<Arguments<C>> argumentsList = new ArrayList<>();
    private final List<CommandOption<?, C>> options = new ArrayList<>();
    private Prerequisite<C> prerequisite = ctx -> {
    };
    private Predicate<C> preprocess = ctx -> true;
    private ContextAction<C> contextAction;
    private final List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers = new ArrayList<>();
    @SuppressWarnings("unchecked")
    private final PlatformAdapter<?, ?, C, B, T> platformAdapter = (PlatformAdapter<?, ?, C, B, T>) PlatformAdapter.get();

    protected CommonCommand(@NotNull String name) {
        Objects.requireNonNull(name);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is not expected to empty.");
        }
        this.name = name;
    }

    public final String name() {
        return name;
    }

    public final String description() {
        return description;
    }

    public final void description(@NotNull String description) {
        this.description = Objects.requireNonNull(description);
    }

    @SafeVarargs
    public final void addChildren(@NotNull T child, @NotNull T... children) {
        List<T> list = new ArrayList<>();
        list.add(child);
        Collections.addAll(list, children);
        addChildren(list);
    }

    public final void addChildren(@NotNull Collection<? extends T> children) {
        Objects.requireNonNull(children);
        validateChildren(children);
        this.children.addAll(children);
        setParentFor(children, null);
    }

    private void validateChildren(@NotNull Collection<? extends T> children) {
        for (T child : children) {
            Objects.requireNonNull(child);
        }
    }

    @SuppressWarnings("unchecked")
    private void setParentFor(@NotNull Collection<? extends T> children, Arguments<C> parentArguments) {
        for (CommonCommand<C, B, T> child : children) {
            child.parent = ((T) this);
            child.parentArguments = parentArguments;
        }
    }

    public final void addAliases(@NotNull String alias, @NotNull String... aliases) {
        List<String> list = new ArrayList<>();
        list.add(alias);
        Collections.addAll(list, aliases);
        addAliases(list);
    }

    public final void addAliases(@NotNull Collection<String> aliases) {
        Objects.requireNonNull(aliases);
        for (String alias : aliases) {
            Objects.requireNonNull(alias);
        }
        this.aliases.addAll(aliases);
    }

    @SuppressWarnings("unchecked")
    public final <E> CommandOption<E, C> option(@NotNull CommandOption<E, ? super C> option) {
        Objects.requireNonNull(option);
        boolean duplicatedName = options.stream()
                                        .anyMatch(x -> x.name()
                                                        .equals(option.name()));
        if (duplicatedName) {
            throw new IllegalArgumentException("Duplicate option name: " + option.name());
        }

        Character shortName = option.shortName();
        if (shortName != null) {
            boolean duplicatedShortName = options.stream()
                                                 .map(CommandOption::shortName)
                                                 .filter(Objects::nonNull)
                                                 .anyMatch(x -> x.equals(shortName));
            if (duplicatedShortName) {
                throw new IllegalArgumentException("Duplicate option short name: " + shortName);
            }
        }

        CommandOption<E, C> typedOption = (CommandOption<E, C>) option;
        options.add(typedOption);
        return typedOption;
    }

    public final void disableParentPrerequisite() {
        this.inheritParentPrerequisite = false;
    }

    public final void disableParentPreprocess() {
        this.inheritParentPreprocess = false;
    }

    public final ArgumentBranch<C, T> argument(@NotNull Consumer<B> buildArguments) {
        B builder = platformAdapter.createArgumentBuilder();
        buildArguments.accept(builder);
        Arguments<C> arguments = addArguments(builder.build(), List.of());
        return new ArgumentBranch<>(delegateFor(arguments));
    }

    public final <T1> UnaryArgumentBranch<T1, C, T> argument(@NotNull CommonArgument<T1, C> argument) {
        Arguments<C> arguments = addArguments(List.of(argument));
        return new UnaryArgumentBranch<>(delegateFor(arguments), argument);
    }

    public final <T1, T2> BiArgumentBranch<T1, T2, C, T> argument(@NotNull CommonArgument<T1, C> argument1,
                                                                  @NotNull CommonArgument<T2, C> argument2) {
        Arguments<C> arguments = addArguments(List.of(argument1, argument2));
        return new BiArgumentBranch<>(delegateFor(arguments), argument1, argument2);
    }

    public final <T1, T2, T3> TriArgumentBranch<T1, T2, T3, C, T> argument(@NotNull CommonArgument<T1, C> argument1,
                                                                           @NotNull CommonArgument<T2, C> argument2,
                                                                           @NotNull CommonArgument<T3, C> argument3) {
        Arguments<C> arguments = addArguments(List.of(argument1, argument2, argument3));
        return new TriArgumentBranch<>(delegateFor(arguments), argument1, argument2, argument3);
    }

    public final <T1, T2, T3, T4> TetraArgumentBranch<T1, T2, T3, T4, C, T> argument(@NotNull CommonArgument<T1, C> argument1,
                                                                                     @NotNull CommonArgument<T2, C> argument2,
                                                                                     @NotNull CommonArgument<T3, C> argument3,
                                                                                     @NotNull CommonArgument<T4, C> argument4) {
        Arguments<C> arguments = addArguments(List.of(argument1, argument2, argument3, argument4));
        return new TetraArgumentBranch<>(delegateFor(arguments), argument1, argument2, argument3, argument4);
    }

    public final <T1, T2, T3, T4, T5> QuintArgumentBranch<T1, T2, T3, T4, T5, C, T> argument(@NotNull CommonArgument<T1, C> argument1,
                                                                                             @NotNull CommonArgument<T2, C> argument2,
                                                                                             @NotNull CommonArgument<T3, C> argument3,
                                                                                             @NotNull CommonArgument<T4, C> argument4,
                                                                                             @NotNull CommonArgument<T5, C> argument5) {
        Arguments<C> arguments = addArguments(List.of(argument1, argument2, argument3, argument4, argument5));
        return new QuintArgumentBranch<>(delegateFor(arguments), argument1, argument2, argument3, argument4, argument5);
    }

    public final <T1, T2, T3, T4, T5, T6> HexaArgumentBranch<T1, T2, T3, T4, T5, T6, C, T> argument(@NotNull CommonArgument<T1, C> argument1,
                                                                                                    @NotNull CommonArgument<T2, C> argument2,
                                                                                                    @NotNull CommonArgument<T3, C> argument3,
                                                                                                    @NotNull CommonArgument<T4, C> argument4,
                                                                                                    @NotNull CommonArgument<T5, C> argument5,
                                                                                                    @NotNull CommonArgument<T6, C> argument6) {
        Arguments<C> arguments = addArguments(List.of(argument1,
                                                      argument2,
                                                      argument3,
                                                      argument4,
                                                      argument5,
                                                      argument6));
        return new HexaArgumentBranch<>(delegateFor(arguments),
                                        argument1,
                                        argument2,
                                        argument3,
                                        argument4,
                                        argument5,
                                        argument6);
    }

    public final <T1, T2, T3, T4, T5, T6, T7> HeptArgumentBranch<T1, T2, T3, T4, T5, T6, T7, C, T> argument(@NotNull CommonArgument<T1, C> argument1,
                                                                                                            @NotNull CommonArgument<T2, C> argument2,
                                                                                                            @NotNull CommonArgument<T3, C> argument3,
                                                                                                            @NotNull CommonArgument<T4, C> argument4,
                                                                                                            @NotNull CommonArgument<T5, C> argument5,
                                                                                                            @NotNull CommonArgument<T6, C> argument6,
                                                                                                            @NotNull CommonArgument<T7, C> argument7) {
        Arguments<C> arguments = addArguments(List.of(argument1,
                                                      argument2,
                                                      argument3,
                                                      argument4,
                                                      argument5,
                                                      argument6,
                                                      argument7));
        return new HeptArgumentBranch<>(delegateFor(arguments),
                                        argument1,
                                        argument2,
                                        argument3,
                                        argument4,
                                        argument5,
                                        argument6,
                                        argument7);
    }

    private ArgumentBranchDelegate<C, T> delegateFor(Arguments<C> arguments) {
        return new ArgumentBranchDelegate<>() {
            @Override
            public void description(@NotNull String description) {
                arguments.description(description);
            }

            @Override
            public void execute(@Nullable ContextAction<C> action) {
                arguments.contextAction(action);
            }

            @Override
            public void addChildren(@NotNull Collection<? extends T> children) {
                addArgumentChildren(arguments, children);
            }
        };
    }

    private Arguments<C> addArguments(@NotNull List<? extends CommonArgument<?, C>> arguments) {
        return addArguments(arguments, List.of());
    }

    private Arguments<C> addArguments(@NotNull List<? extends CommonArgument<?, C>> arguments,
                                      @NotNull Collection<? extends T> children) {
        validateArguments(arguments);
        validateChildren(children);
        Arguments<C> argumentBranch = new Arguments<>(arguments, children);
        argumentsList.add(argumentBranch);
        setParentFor(children, argumentBranch);
        return argumentBranch;
    }

    final void addArgumentChildren(@NotNull Arguments<C> arguments, @NotNull Collection<? extends T> children) {
        Objects.requireNonNull(arguments);
        Objects.requireNonNull(children);
        validateChildren(children);
        arguments.addChildren(children);
        setParentFor(children, arguments);
    }

    private void validateArguments(@NotNull List<? extends CommonArgument<?, C>> arguments) {
        for (CommonArgument<?, C> argument : arguments) {
            Objects.requireNonNull(argument);
        }
        Set<String> names = new HashSet<>();
        for (CommonArgument<?, C> argument : arguments) {
            if (!names.add(argument.name())) {
                throw new IllegalArgumentException("Duplicate argument name: " + argument.name());
            }
        }
    }

    public final void addPrerequisite(@NotNull Prerequisite<C> prerequisite) {
        this.prerequisite = this.prerequisite.and(Objects.requireNonNull(prerequisite));
    }

    public final void addPreprocess(@NotNull Consumer<C> preprocess) {
        Objects.requireNonNull(preprocess);

        addPreprocess(ctx -> {
            preprocess.accept(ctx);
            return true;
        });
    }

    /**
     * @param preprocess return false if cancel executing command.
     */
    public final void addPreprocess(@NotNull Predicate<C> preprocess) {
        this.preprocess = this.preprocess.and(Objects.requireNonNull(preprocess));
    }

    public final void execute(@Nullable ContextAction<C> execute) {
        this.contextAction = execute;
    }

    public final void addUncaughtExceptionHandler(UncaughtExceptionHandler<?, C> handler) {
        this.uncaughtExceptionHandlers.add(handler);
    }

    final T parent() {
        return parent;
    }

    final Arguments<C> parentArguments() {
        return parentArguments;
    }

    final List<T> children() {
        return List.copyOf(children);
    }

    @SuppressWarnings("unchecked")
    final List<T> argumentChildren() {
        return argumentsList.stream()
                            .flatMap(x -> x.children()
                                           .stream())
                            .map(x -> (T) x)
                            .collect(java.util.stream.Collectors.toList());
    }

    final Prerequisite<C> prerequisite() {
        if (!inheritParentPrerequisite || parent == null) {
            return prerequisite;
        }

        return parent.prerequisite()
                     .and(prerequisite);
    }

    final Predicate<C> preprocess() {
        if (!inheritParentPreprocess || parent == null) {
            return preprocess;
        }

        return parent.preprocess()
                     .and(preprocess);
    }

    final List<Arguments<C>> argumentsList() {
        return List.copyOf(argumentsList);
    }

    final List<CommandOption<?, C>> options() {
        return List.copyOf(options);
    }

    final List<String> aliases() {
        return List.copyOf(aliases);
    }

    final ContextAction<C> contextAction() {
        return contextAction;
    }

    final List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers() {
        return List.copyOf(uncaughtExceptionHandlers);
    }
}
