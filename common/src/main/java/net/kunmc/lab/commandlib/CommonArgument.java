package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.exception.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
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
    private Predicate<? super T> filter;
    private Function<? super T, ? extends T> shaper;

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

    protected final void setDisplayDefaultSuggestions(boolean display) {
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

    protected final void setSuggestionAction(@Nullable SuggestionAction<C> suggestionAction) {
        this.suggestionAction = suggestionAction;
    }

    protected final void setAdditionalSuggestionAction(@Nullable SuggestionAction<C> additionalSuggestionAction) {
        this.additionalSuggestionAction = additionalSuggestionAction;
    }

    protected final void setAdditionalParser(@Nullable BiFunction<C, String, T> parser) {
        this.additionalParser = parser;
    }

    @Nullable
    public final ContextAction<C> contextAction() {
        return contextAction;
    }

    protected final void setContextAction(@Nullable ContextAction<C> contextAction) {
        this.contextAction = contextAction;
    }

    public final ArgumentType<?> type() {
        return type;
    }

    public abstract T cast(Object parsedArgument);

    protected final void setFilter(Predicate<? super T> filter) {
        this.filter = filter;
    }

    protected final void setShaper(Function<? super T, ? extends T> shaper) {
        this.shaper = shaper;
    }

    protected final void applyOptions(@Nullable Consumer<Option<T, C>> options) {
        if (options == null) {
            return;
        }
        Option<T, C> option = new Option<>();
        options.accept(option);
        applyOption(option);
    }

    protected final void applyOption(@NotNull Option<T, C> option) {
        setDisplayDefaultSuggestions(option.isDisplayDefaultSuggestions());
        option.suggestionAction()
              .ifPresent(this::setSuggestionAction);
        option.additionalSuggestionAction()
              .ifPresent(this::setAdditionalSuggestionAction);
        option.additionalParser()
              .ifPresent(this::setAdditionalParser);
        option.contextAction()
              .ifPresent(this::setContextAction);
        option.filter()
              .ifPresent(this::setFilter);
        option.shaper()
              .ifPresent(this::setShaper);
    }

    @NotNull
    protected final Predicate<? super T> filter() {
        if (filter == null) {
            return x -> true;
        }
        return x -> {
            try {
                return filter.test(x);
            } catch (InvalidArgumentException e) {
                return false;
            }
        };
    }

    @NotNull
    final T parse(C ctx) throws IncorrectArgumentInputException {
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
        } catch (IncorrectArgumentInputException e) {
            if (additionalParser != null) {
                t = additionalParser.apply(ctx, ctx.getInput(name));
            }

            if (t == null) {
                throw e;
            }
        }

        try {
            if (filter != null && !filter.test(t)) {
                throw new IncorrectArgumentInputException(this, ctx, ctx.getInput(name));
            }
        } catch (InvalidArgumentException e) {
            throw e.toIncorrectArgumentInputException();
        }

        if (shaper == null) {
            return t;
        }
        return shaper.apply(t);
    }

    protected abstract T parseImpl(C ctx) throws CommandSyntaxException, IncorrectArgumentInputException;

    public static class Option<T, C extends AbstractCommandContext<?, ?>> {
        protected boolean displayDefaultSuggestions = true;
        protected SuggestionAction<C> suggestionAction;
        /**
         * It can be used to add additional suggests to an argument that configures a suggestion action by default, such as {@link net.kunmc.lab.commandlib.argument.CommonObjectArgument}.
         */
        protected SuggestionAction<C> additionalSuggestionAction;
        protected BiFunction<C, String, T> additionalParser;
        protected Predicate<? super T> filter;
        protected Function<? super T, ? extends T> shaper;
        protected ContextAction<C> contextAction;

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
         * Filtering values on tab completion and after parsing.<br>
         * Throwing {@link net.kunmc.lab.commandlib.exception.InvalidArgumentException}, you can customize the error message.
         */
        public Option<T, C> filter(@Nullable Consumer<? super T> filter) {
            return filter(x -> {
                if (filter != null) {
                    filter.accept(x);
                }
                return true;
            });
        }

        /**
         * Filtering values on tab completion and after parsing.<br>
         * Throwing {@link net.kunmc.lab.commandlib.exception.InvalidArgumentException}, you can customize the error message.
         */
        public Option<T, C> filter(@Nullable Predicate<? super T> filter) {
            this.filter = filter;
            return this;
        }

        protected Optional<Predicate<? super T>> filter() {
            return Optional.ofNullable(filter);
        }

        public Option<T, C> shaper(@Nullable Function<? super T, ? extends T> shaper) {
            this.shaper = shaper;
            return this;
        }

        protected Optional<Function<? super T, ? extends T>> shaper() {
            return Optional.ofNullable(shaper);
        }

        public Option<T, C> contextAction(@Nullable ContextAction<C> contextAction) {
            this.contextAction = contextAction;
            return this;
        }

        protected Optional<ContextAction<C>> contextAction() {
            return Optional.ofNullable(contextAction);
        }
    }
}
