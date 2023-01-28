package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class CommonArgument<T, C extends AbstractCommandContext<?, ?>> {
    protected final String name;
    private boolean displayDefaultSuggestions = true;
    private SuggestionAction<C> suggestionAction;
    private SuggestionAction<C> additionalSuggestionAction;
    private BiFunction<C, String, T> additionalParser;
    private ContextAction<C> contextAction;
    private final ArgumentType<?> type;
    private Predicate<? super T> filter;
    private Function<? super T, ? extends T> shaper;

    public CommonArgument(String name, ArgumentType<?> type) {
        this.name = name;
        this.type = type;
    }

    public CommonArgument(String name,
                          SuggestionAction<C> suggestionAction,
                          ContextAction<C> contextAction,
                          ArgumentType<?> type) {
        this.name = name;
        this.suggestionAction = suggestionAction;
        this.contextAction = contextAction;
        this.type = type;
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

    protected final void setSuggestionAction(SuggestionAction<C> suggestionAction) {
        this.suggestionAction = suggestionAction;
    }

    protected final void setAdditionalSuggestionAction(SuggestionAction<C> additionalSuggestionAction) {
        this.additionalSuggestionAction = additionalSuggestionAction;
    }

    protected final void setAdditionalParser(BiFunction<C, String, T> parser) {
        this.additionalParser = parser;
    }

    public final ContextAction<C> contextAction() {
        return contextAction;
    }

    protected final void setContextAction(ContextAction<C> contextAction) {
        this.contextAction = contextAction;
    }

    public final ArgumentType<?> type() {
        return type;
    }

    public abstract T cast(Object parsedArgument);

    final boolean hasContextAction() {
        return contextAction != null;
    }

    protected final Predicate<? super T> filter() {
        return filter;
    }

    protected final void setFilter(Predicate<? super T> filter) {
        this.filter = filter;
    }

    protected final void setShaper(Function<? super T, ? extends T> shaper) {
        this.shaper = shaper;
    }

    protected final void setOptions(Consumer<Option<T, C>> options) {
        if (options == null) {
            return;
        }
        Option<T, C> option = new Option<>();
        options.accept(option);
        setOption(option);
    }

    protected final void setOption(Option<T, C> option) {
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

    final T parseInternal(C ctx) throws IncorrectArgumentInputException {
        T t = null;
        try {
            t = parse(ctx);
        } catch (CommandSyntaxException e) {
            if (additionalParser != null) {
                t = additionalParser.apply(ctx, ctx.getInput(name));
            }

            if (t == null) {
                throw ctx.platformAdapter()
                         .convertCommandSyntaxException(e);
            }
        }

        if (filter != null && !filter.test(t)) {
            throw new IncorrectArgumentInputException(this, ctx, ctx.getInput(name));
        }

        if (shaper == null) {
            return t;
        }
        return shaper.apply(t);
    }

    public abstract T parse(C ctx) throws CommandSyntaxException, IncorrectArgumentInputException;

    @Accessors(chain = true, fluent = true)
    @Setter
    public static class Option<T, C extends AbstractCommandContext<?, ?>> {
        protected boolean displayDefaultSuggestions = true;
        protected SuggestionAction<C> suggestionAction;
        protected SuggestionAction<C> additionalSuggestionAction;
        protected BiFunction<C, String, T> additionalParser;
        protected Predicate<? super T> filter;
        protected Function<? super T, ? extends T> shaper;
        protected ContextAction<C> contextAction;

        protected boolean isDisplayDefaultSuggestions() {
            return displayDefaultSuggestions;
        }

        protected Optional<SuggestionAction<C>> suggestionAction() {
            return Optional.ofNullable(suggestionAction);
        }

        protected Optional<SuggestionAction<C>> additionalSuggestionAction() {
            return Optional.ofNullable(additionalSuggestionAction);
        }

        protected Optional<BiFunction<C, String, T>> additionalParser() {
            return Optional.ofNullable(additionalParser);
        }

        protected Optional<Predicate<? super T>> filter() {
            return Optional.ofNullable(filter);
        }

        protected Optional<Function<? super T, ? extends T>> shaper() {
            return Optional.ofNullable(shaper);
        }

        protected Optional<ContextAction<C>> contextAction() {
            return Optional.ofNullable(contextAction);
        }
    }
}
