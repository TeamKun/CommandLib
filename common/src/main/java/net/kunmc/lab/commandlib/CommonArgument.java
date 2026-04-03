package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.exception.ArgumentValidationException;
import net.kunmc.lab.commandlib.util.UncaughtExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class CommonArgument<T, C extends AbstractCommandContext<?, ?>> {
    private final String name;
    private boolean displayDefaultSuggestions = true;
    private SuggestionAction<C> suggestionAction;
    private SuggestionAction<C> additionalSuggestionAction;
    private BiFunction<C, String, T> additionalParser;
    private ContextAction<C> contextAction;
    private final ArgumentType<?> type;
    private ArgumentValidator<? super T, C> validator;
    private BiFunction<? super T, C, ? extends T> transformer;
    private final List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers = new ArrayList<>();

    protected CommonArgument(@NotNull String name, @NotNull ArgumentType<?> type) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    protected CommonArgument(@NotNull String name,
                             @Nullable SuggestionAction<C> suggestionAction,
                             @Nullable ContextAction<C> contextAction,
                             @NotNull ArgumentType<?> type) {
        this.name = Objects.requireNonNull(name);
        this.suggestionAction = suggestionAction;
        this.contextAction = contextAction;
        this.type = Objects.requireNonNull(type);
    }

    public final String name() {
        return name;
    }

    final boolean isDisplayDefaultSuggestions() {
        return displayDefaultSuggestions;
    }

    protected final void displayDefaultSuggestions(boolean display) {
        this.displayDefaultSuggestions = display;
    }

    public final SuggestionAction<C> suggestionAction() {
        if (suggestionAction == null && additionalSuggestionAction == null) {
            return null;
        }

        return sb -> {
            if (suggestionAction != null) {
                suggestionAction.accept(sb);
            }
            if (additionalSuggestionAction != null) {
                additionalSuggestionAction.accept(sb);
            }
        };
    }

    protected final void suggestionAction(@Nullable SuggestionAction<C> suggestionAction) {
        this.suggestionAction = suggestionAction;
    }

    protected final void additionalSuggestionAction(@Nullable SuggestionAction<C> additionalSuggestionAction) {
        this.additionalSuggestionAction = additionalSuggestionAction;
    }

    protected final void additionalParser(@Nullable BiFunction<C, String, T> parser) {
        this.additionalParser = parser;
    }

    @Nullable
    public final ContextAction<C> contextAction() {
        return contextAction;
    }

    public final List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers() {
        return Collections.unmodifiableList(uncaughtExceptionHandlers);
    }

    protected final void contextAction(@Nullable ContextAction<C> contextAction) {
        this.contextAction = contextAction;
    }

    public final ArgumentType<?> type() {
        return type;
    }

    public abstract T cast(Object parsedArgument);

    protected final void validator(ArgumentValidator<? super T, C> validator) {
        this.validator = validator;
    }

    protected final void transformer(Function<? super T, ? extends T> transformer) {
        transformer((value, ctx) -> transformer.apply(value));
    }

    protected final void transformer(BiFunction<? super T, C, ? extends T> transformer) {
        this.transformer = transformer;
    }

    protected final void applyOptions(@Nullable Consumer<Option<T, C>> options) {
        if (options == null) {
            return;
        }
        Option<T, C> option = new Option<>(this);
        options.accept(option);
        applyOption(option);
    }

    protected final void applyOption(@NotNull Option<T, C> option) {
        displayDefaultSuggestions(option.isDisplayDefaultSuggestions());
        option.suggestionAction()
              .ifPresent(this::suggestionAction);
        option.additionalSuggestionAction()
              .ifPresent(this::additionalSuggestionAction);
        option.additionalParser()
              .ifPresent(this::additionalParser);
        option.contextAction()
              .ifPresent(this::contextAction);
        option.validator()
              .ifPresent(this::validator);
        option.transformer()
              .ifPresent(this::transformer);
        this.uncaughtExceptionHandlers.addAll(option.uncaughtExceptionHandlers());
    }

    @NotNull
    protected final Predicate<? super T> filter(C ctx) {
        if (validator == null) {
            return (x) -> true;
        }
        return (x) -> {
            try {
                validator.validate(x, ctx);
                return true;
            } catch (ArgumentParseException e) {
                return false;
            }
        };
    }

    @NotNull
    final T parse(C ctx) throws ArgumentParseException {
        T t = null;
        try {
            t = Objects.requireNonNull(parseImpl(ctx));
        } catch (CommandSyntaxException e) {
            if (additionalParser != null) {
                t = additionalParser.apply(ctx, ctx.getInput(name));
            }

            if (t == null) {
                throw PlatformAdapter.get()
                                     .convertCommandSyntaxException(e);
            }
        } catch (ArgumentParseException e) {
            if (additionalParser != null) {
                t = additionalParser.apply(ctx, ctx.getInput(name));
            }

            if (t == null) {
                throw e;
            }
        }

        if (validator != null) {
            validator.validate(t, ctx);
        }

        if (transformer == null) {
            return t;
        }
        return transformer.apply(t, ctx);
    }

    protected abstract T parseImpl(C ctx) throws CommandSyntaxException, ArgumentParseException;

    public static class Option<T, C extends AbstractCommandContext<?, ?>> {
        protected boolean displayDefaultSuggestions = true;
        protected SuggestionAction<C> suggestionAction;
        /**
         * It can be used to add additional suggests to an argument that configures a suggestion action by default, such as {@link net.kunmc.lab.commandlib.argument.CommonObjectArgument}.
         */
        protected SuggestionAction<C> additionalSuggestionAction;
        protected BiFunction<C, String, T> additionalParser;
        protected ArgumentValidator<? super T, C> validator;
        protected BiFunction<? super T, C, ? extends T> transformer;
        protected ContextAction<C> contextAction;
        protected final List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers = new ArrayList<>();
        private final CommonArgument<T, C> argument;

        public Option(CommonArgument<T, C> argument) {
            this.argument = argument;
        }

        public Option<T, C> displayDefaultSuggestions(boolean displayDefaultSuggestions) {
            this.displayDefaultSuggestions = displayDefaultSuggestions;
            return this;
        }

        protected boolean isDisplayDefaultSuggestions() {
            return displayDefaultSuggestions;
        }

        public Option<T, C> suggestionAction(@Nullable SuggestionAction<C> suggestionAction) {
            this.suggestionAction = suggestionAction;
            return this;
        }

        protected Optional<SuggestionAction<C>> suggestionAction() {
            return Optional.ofNullable(suggestionAction);
        }

        public Option<T, C> additionalSuggestionAction(@Nullable SuggestionAction<C> additionalSuggestionAction) {
            this.additionalSuggestionAction = additionalSuggestionAction;
            return this;
        }

        protected Optional<SuggestionAction<C>> additionalSuggestionAction() {
            return Optional.ofNullable(additionalSuggestionAction);
        }

        public Option<T, C> additionalParser(@Nullable BiFunction<C, String, T> additionalParser) {
            this.additionalParser = additionalParser;
            return this;
        }

        protected Optional<BiFunction<C, String, T>> additionalParser() {
            return Optional.ofNullable(additionalParser);
        }

        /**
         * Validates values on tab completion and after parsing.<br>
         */
        public Option<T, C> validator(@Nullable Predicate<? super T> validator) {
            if (validator == null) {
                this.validator = null;
            } else {
                validator((x, ctx) -> {
                    if (!validator.test(x)) {
                        throw ArgumentValidationException.ofIncorrectInput(argument.name(),
                                                                           ctx,
                                                                           ctx.getInput(argument.name()));
                    }
                });
            }

            return this;
        }

        /**
         * Validates values on tab completion and after parsing.<br>
         */
        public Option<T, C> validator(@Nullable BiFunction<? super T, C, Boolean> validator) {
            if (validator == null) {
                this.validator = null;
            } else {
                validator((x, ctx) -> {
                    if (!validator.apply(x, ctx)) {
                        throw ArgumentValidationException.ofIncorrectInput(argument.name(),
                                                                           ctx,
                                                                           ctx.getInput(argument.name()));
                    }
                });
            }

            return this;
        }

        /**
         * Validates values on tab completion and after parsing.<br>
         * Throwing {@link net.kunmc.lab.commandlib.exception.ArgumentValidationException}, you can customize the error message.
         */
        public Option<T, C> validator(@Nullable ArgumentValidator<? super T, C> validator) {
            this.validator = validator;
            return this;
        }

        protected Optional<ArgumentValidator<? super T, C>> validator() {
            return Optional.ofNullable(validator);
        }

        public Option<T, C> transformer(@Nullable Function<? super T, ? extends T> transformer) {
            if (transformer == null) {
                this.transformer = null;
            } else {
                transformer((x, ctx) -> transformer.apply(x));
            }

            return this;
        }

        public Option<T, C> transformer(@Nullable BiFunction<? super T, C, ? extends T> transformer) {
            this.transformer = transformer;
            return this;
        }

        protected Optional<BiFunction<? super T, C, ? extends T>> transformer() {
            return Optional.ofNullable(transformer);
        }

        public Option<T, C> contextAction(@Nullable ContextAction<C> contextAction) {
            this.contextAction = contextAction;
            return this;
        }

        protected Optional<ContextAction<C>> contextAction() {
            return Optional.ofNullable(contextAction);
        }

        public Option<T, C> addUncaughtExceptionHandler(UncaughtExceptionHandler<?, C> handler) {
            this.uncaughtExceptionHandlers.add(handler);
            return this;
        }

        protected List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers() {
            return uncaughtExceptionHandlers;
        }
    }
}
