package net.kunmc.lab.commandlib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class AbstractArgumentBuilder<C extends AbstractCommandContext<?, ?>, A extends AbstractArguments<?, C>, T extends AbstractArgumentBuilder<C, A, T>> {
    protected final List<CommonArgument<?, C>> arguments = new ArrayList<>();
    private ContextAction<C> contextAction = null;

    /**
     * Add any argument that implements {@link net.kunmc.lab.commandlib.CommonArgument}.
     */
    public final <E> T customArgument(@NotNull CommonArgument<E, C> argument) {
        arguments.add(argument);
        return ((T) this);
    }

    /**
     * Add any argument that implements {@link net.kunmc.lab.commandlib.CommonArgument}.
     */
    public final <E> T customArgument(@NotNull CommonArgument<E, C> argument,
                                      @Nullable ContextAction<C> contextAction) {
        arguments.add(argument);
        argument.setContextAction(contextAction);
        return ((T) this);
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public final <E extends Enum<E>> T enumArgument(@NotNull String name, @NotNull Class<E> clazz) {
        return enumArgument(name, clazz, null);
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public final <E extends Enum<E>> T enumArgument(@NotNull String name,
                                                    @NotNull Class<E> clazz,
                                                    @Nullable Predicate<? super E> filter) {
        return enumArgument(name, clazz, filter, null);
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public final <E extends Enum<E>> T enumArgument(@NotNull String name,
                                                    @NotNull Class<E> clazz,
                                                    @Nullable Predicate<? super E> filter,
                                                    @Nullable ContextAction<C> contextAction) {
        return enumArgumentWith(name, clazz, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public abstract <E extends Enum<E>> T enumArgumentWith(@NotNull String name,
                                                           @NotNull Class<E> clazz,
                                                           @Nullable Consumer<CommonArgument.Option<E, C>> options);

    /**
     * Add argument for {@link java.lang.String}.<br>
     * It is only possible to include a string specified by {@code literals}
     */
    public final T literalArgument(@NotNull String name, @NotNull Collection<String> literals) {
        return literalArgument(name, literals, null);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * It is only possible to include a string specified by {@code literals}
     */
    public final T literalArgument(@NotNull String name,
                                   @NotNull Collection<String> literals,
                                   @Nullable ContextAction<C> contextAction) {
        return literalArgument(name, () -> literals, contextAction);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * It is only possible to include a string specified by {@code literals}
     */
    public final T literalArgument(@NotNull String name, @NotNull Supplier<Collection<String>> literalsSupplier) {
        return literalArgument(name, literalsSupplier, null);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * It is only possible to include a string specified by {@code literals}
     */
    public abstract T literalArgument(@NotNull String name,
                                      @NotNull Supplier<Collection<String>> literalsSupplier,
                                      @Nullable ContextAction<C> contextAction);

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public final <E extends Nameable> T nameableObjectArgument(@NotNull String name,
                                                               @NotNull Collection<? extends E> candidates) {
        return nameableObjectArgument(name, candidates, null);
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public final <E extends Nameable> T nameableObjectArgument(@NotNull String name,
                                                               @NotNull Collection<? extends E> candidates,
                                                               @Nullable Predicate<? super E> filter) {
        return nameableObjectArgument(name, candidates, filter, null);
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public final <E extends Nameable> T nameableObjectArgument(@NotNull String name,
                                                               @NotNull Collection<? extends E> candidates,
                                                               @Nullable Predicate<? super E> filter,
                                                               @Nullable ContextAction<C> contextAction) {
        return nameableObjectArgumentWith(name, candidates, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public abstract <E extends Nameable> T nameableObjectArgumentWith(@NotNull String name,
                                                                      @NotNull Collection<? extends E> candidates,
                                                                      @Nullable Consumer<CommonArgument.Option<E, C>> options);


    public final <E> T objectArgument(@NotNull String name, @NotNull Map<String, ? extends E> nameToObjectMap) {
        return objectArgumentWith(name, nameToObjectMap, options -> {
        });
    }

    public final <E> T objectArgument(@NotNull String name,
                                      @NotNull Map<String, ? extends E> nameToObjectMap,
                                      @Nullable Predicate<? super E> filter) {
        return objectArgument(name, nameToObjectMap, filter, null);
    }

    public final <E> T objectArgument(@NotNull String name,
                                      @NotNull Map<String, ? extends E> nameToObjectMap,
                                      @Nullable Predicate<? super E> filter,
                                      @Nullable ContextAction<C> contextAction) {
        return objectArgumentWith(name, nameToObjectMap, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    public abstract <E> T objectArgumentWith(@NotNull String name,
                                             @NotNull Map<String, ? extends E> nameToObjectMap,
                                             @Nullable Consumer<CommonArgument.Option<E, C>> options);

    /**
     * Set command's process.<br>
     * If arguments are not added, process set by this wouldn't work. Then you should override {@link net.kunmc.lab.commandlib.CommonCommand#execute(AbstractCommandContext)} or use {@link net.kunmc.lab.commandlib.CommonCommand#execute(ContextAction)}
     */
    public final void execute(@NotNull ContextAction<C> contextAction) {
        this.contextAction = contextAction;
    }

    final A build() {
        if (!arguments.isEmpty()) {
            CommonArgument<?, C> last = arguments.get(arguments.size() - 1);
            if (!last.hasContextAction()) {
                last.setContextAction(contextAction);
            }
        }

        return createArguments(arguments);
    }

    abstract A createArguments(List<CommonArgument<?, C>> arguments);
}
