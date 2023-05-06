package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

abstract class AbstractArgumentBuilder<C extends AbstractCommandContext<?, ?>, T extends AbstractArgumentBuilder<C, T>> {
    private final List<CommonArgument<?, C>> arguments = new ArrayList<>();
    private ContextAction<C> contextAction = null;

    protected final T addArgument(@NotNull CommonArgument<?, C> argument) {
        arguments.add(argument);
        return (T) this;
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public final T boolArgument(@NotNull String name) {
        return boolArgument(name, null, null);
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public final T boolArgument(@NotNull String name, @Nullable SuggestionAction<C> suggestionAction) {
        return boolArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public final T boolArgument(@NotNull String name,
                                @Nullable SuggestionAction<C> suggestionAction,
                                @Nullable ContextAction<C> contextAction) {
        return boolArgumentWith(name, options -> {
            options.suggestionAction(suggestionAction)
                   .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public final T boolArgumentWith(@NotNull String name,
                                    @Nullable Consumer<CommonArgument.Option<Boolean, C>> options) {
        return addArgument(new CommonBooleanArgument<>(name, options));
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public final T doubleArgument(@NotNull String name) {
        return doubleArgument(name, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public final T doubleArgument(@NotNull String name, Double min, Double max) {
        return doubleArgument(name, min, max, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public final T doubleArgument(@NotNull String name, @Nullable SuggestionAction<C> suggestionAction) {
        return doubleArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public final T doubleArgument(@NotNull String name,
                                  @Nullable SuggestionAction<C> suggestionAction,
                                  @Nullable ContextAction<C> contextAction) {
        return doubleArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public final T doubleArgument(@NotNull String name,
                                  Double min,
                                  Double max,
                                  @Nullable SuggestionAction<C> suggestionAction) {
        return doubleArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public final T doubleArgument(@NotNull String name,
                                  Double min,
                                  Double max,
                                  @Nullable SuggestionAction<C> suggestionAction,
                                  @Nullable ContextAction<C> contextAction) {
        return doubleArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public final T doubleArgumentWith(@NotNull String name,
                                      @Nullable Consumer<CommonArgument.Option<Double, C>> options) {
        return addArgument(new CommonDoubleArgument<>(name, options));
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public final T doubleArgumentWith(@NotNull String name,
                                      @Nullable Consumer<CommonArgument.Option<Double, C>> options,
                                      Double min,
                                      Double max) {
        return addArgument(new CommonDoubleArgument<>(name, options, min, max));
    }

    /**
     * Add any argument that implements {@link net.kunmc.lab.commandlib.CommonArgument}.
     */
    public final <E> T customArgument(@NotNull CommonArgument<E, C> argument) {
        return addArgument(argument);
    }

    /**
     * Add any argument that implements {@link net.kunmc.lab.commandlib.CommonArgument}.
     */
    public final <E> T customArgument(@NotNull CommonArgument<E, C> argument,
                                      @Nullable ContextAction<C> contextAction) {
        argument.setContextAction(contextAction);
        return addArgument(argument);
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
    public final <E extends Enum<E>> T enumArgumentWith(@NotNull String name,
                                                        @NotNull Class<E> clazz,
                                                        @Nullable Consumer<CommonArgument.Option<E, C>> options) {
        return addArgument(new CommonEnumArgument<>(name, clazz, options));
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public final T floatArgument(@NotNull String name) {
        return floatArgument(name, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public final T floatArgument(@NotNull String name, Float min, Float max) {
        return floatArgument(name, min, max, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public final T floatArgument(@NotNull String name, @Nullable SuggestionAction<C> suggestionAction) {
        return floatArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public final T floatArgument(@NotNull String name,
                                 @Nullable SuggestionAction<C> suggestionAction,
                                 @Nullable ContextAction<C> contextAction) {
        return floatArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public final T floatArgument(@NotNull String name,
                                 Float min,
                                 Float max,
                                 @Nullable SuggestionAction<C> suggestionAction) {
        return floatArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public final T floatArgument(@NotNull String name,
                                 Float min,
                                 Float max,
                                 @Nullable SuggestionAction<C> suggestionAction,
                                 @Nullable ContextAction<C> contextAction) {
        return floatArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public final T floatArgumentWith(@NotNull String name,
                                     @Nullable Consumer<CommonArgument.Option<Float, C>> options) {
        return addArgument(new CommonFloatArgument<>(name, options));
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public final T floatArgumentWith(@NotNull String name,
                                     @Nullable Consumer<CommonArgument.Option<Float, C>> options,
                                     Float min,
                                     Float max) {
        return addArgument(new CommonFloatArgument<>(name, options, min, max));
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public final T integerArgument(@NotNull String name) {
        return integerArgument(name, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public final T integerArgument(@NotNull String name, Integer min, Integer max) {
        return integerArgument(name, min, max, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public final T integerArgument(@NotNull String name, @Nullable SuggestionAction<C> suggestionAction) {
        return integerArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public final T integerArgument(@NotNull String name,
                                   @Nullable SuggestionAction<C> suggestionAction,
                                   @Nullable ContextAction<C> contextAction) {
        return integerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public final T integerArgument(@NotNull String name,
                                   Integer min,
                                   Integer max,
                                   @Nullable SuggestionAction<C> suggestionAction) {
        return integerArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public final T integerArgument(@NotNull String name,
                                   Integer min,
                                   Integer max,
                                   @Nullable SuggestionAction<C> suggestionAction,
                                   @Nullable ContextAction<C> contextAction) {
        return integerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public final T integerArgumentWith(@NotNull String name,
                                       @Nullable Consumer<CommonArgument.Option<Integer, C>> options) {
        return addArgument(new CommonIntegerArgument<>(name, options));
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public final T integerArgumentWith(@NotNull String name,
                                       @Nullable Consumer<CommonArgument.Option<Integer, C>> options,
                                       Integer min,
                                       Integer max) {
        return addArgument(new CommonIntegerArgument<>(name, options, min, max));
    }

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
    public final T literalArgument(@NotNull String name,
                                   @NotNull Supplier<Collection<String>> literalsSupplier,
                                   @Nullable ContextAction<C> contextAction) {
        return addArgument(new CommonLiteralArgument<>(name, literalsSupplier, contextAction));
    }

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
    public final <E extends Nameable> T nameableObjectArgumentWith(@NotNull String name,
                                                                   @NotNull Collection<? extends E> candidates,
                                                                   @Nullable Consumer<CommonArgument.Option<E, C>> options) {
        return addArgument(new CommonNameableObjectArgument<>(name, candidates, options));
    }


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

    public final <E> T objectArgumentWith(@NotNull String name,
                                          @NotNull Map<String, ? extends E> nameToObjectMap,
                                          @Nullable Consumer<CommonArgument.Option<E, C>> options) {
        return addArgument(new CommonObjectArgument<>(name, nameToObjectMap, options));
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public final T stringArgument(@NotNull String name) {
        return stringArgument(name, ((@Nullable SuggestionAction<C>) null));
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public final T stringArgument(@NotNull String name, @NotNull CommonStringArgument.Type type) {
        return stringArgument(name, type, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public final T stringArgument(@NotNull String name, @Nullable SuggestionAction<C> suggestionAction) {
        return stringArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public final T stringArgument(@NotNull String name,
                                  @Nullable SuggestionAction<C> suggestionAction,
                                  @Nullable ContextAction<C> contextAction) {
        return stringArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public final T stringArgument(@NotNull String name,
                                  @NotNull CommonStringArgument.Type type,
                                  @Nullable SuggestionAction<C> suggestionAction) {
        return stringArgument(name, type, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public final T stringArgument(@NotNull String name,
                                  @NotNull CommonStringArgument.Type type,
                                  @Nullable SuggestionAction<C> suggestionAction,
                                  @Nullable ContextAction<C> contextAction) {
        return stringArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, type);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public final T stringArgumentWith(@NotNull String name,
                                      @Nullable Consumer<CommonArgument.Option<String, C>> options) {
        return addArgument(new CommonStringArgument<>(name, options));
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public final T stringArgumentWith(@NotNull String name,
                                      @Nullable Consumer<CommonArgument.Option<String, C>> options,
                                      @NotNull CommonStringArgument.Type type) {
        return addArgument(new CommonStringArgument<>(name, options, type));
    }

    /**
     * Set command's process.<br>
     * If arguments are not added, process set by this wouldn't work. Then you should use {@link net.kunmc.lab.commandlib.CommonCommand#execute(ContextAction)}
     */
    public final void execute(@NotNull ContextAction<C> contextAction) {
        this.contextAction = contextAction;
    }

    final List<CommonArgument<?, C>> build() {
        if (!arguments.isEmpty()) {
            CommonArgument<?, C> last = arguments.get(arguments.size() - 1);
            if (last.isContextActionUndefined()) {
                last.setContextAction(contextAction);
            }
        }

        return arguments;
    }
}
