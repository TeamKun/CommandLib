package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Argument<T> {
    protected final String name;
    private SuggestionAction suggestionAction;
    private SuggestionAction additionalSuggestionAction;
    private ContextAction contextAction;
    private final ArgumentType<?> type;
    private Predicate<? super T> filter;
    private Function<? super T, ? extends T> shaper;
    private Function<CommandContext, IncorrectArgumentInputException> inputExceptionByFilterGenerator;

    public Argument(String name, ArgumentType<?> type) {
        this.name = name;
        this.type = type;
    }

    public Argument(String name, SuggestionAction suggestionAction, ContextAction contextAction, ArgumentType<?> type) {
        this.name = name;
        this.suggestionAction = suggestionAction;
        this.contextAction = contextAction;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public SuggestionAction suggestionAction() {
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

    protected void setSuggestionAction(SuggestionAction suggestionAction) {
        this.suggestionAction = suggestionAction;
    }

    protected void setAdditionalSuggestionAction(SuggestionAction additionalSuggestionAction) {
        this.additionalSuggestionAction = additionalSuggestionAction;
    }

    public ContextAction contextAction() {
        return contextAction;
    }

    protected void setContextAction(ContextAction contextAction) {
        this.contextAction = contextAction;
    }

    public ArgumentType<?> type() {
        return type;
    }

    public abstract T cast(Object parsedArgument);

    boolean hasContextAction() {
        return contextAction != null;
    }

    protected Predicate<? super T> filter() {
        return filter;
    }

    protected void setFilter(Predicate<? super T> filter) {
        this.filter = filter;
    }

    protected void setShaper(Function<? super T, ? extends T> shaper) {
        this.shaper = shaper;
    }

    protected void setOptions(Consumer<Option<T>> options) {
        if (options == null) {
            return;
        }
        Option<T> option = new Option<>();
        options.accept(option);
        setOption(option);
    }

    protected void setOption(Option<T> option) {
        option.suggestionAction()
              .ifPresent(this::setSuggestionAction);
        option.additionalSuggestionAction()
              .ifPresent(this::setAdditionalSuggestionAction);
        option.contextAction()
              .ifPresent(this::setContextAction);
        option.filter()
              .ifPresent(this::setFilter);
        option.shaper()
              .ifPresent(this::setShaper);
    }

    protected void setInputExceptionByFilterGenerator(Function<CommandContext, IncorrectArgumentInputException> inputExceptionByFilterGenerator) {
        this.inputExceptionByFilterGenerator = inputExceptionByFilterGenerator;
    }

    final T parseInternal(CommandContext ctx) throws IncorrectArgumentInputException {
        T t;
        try {
            t = parse(ctx);
        } catch (CommandSyntaxException e) {
            throw IncorrectArgumentInputException.fromCommandSyntaxException(e);
        }

        if (filter != null && !filter.test(t)) {
            if (inputExceptionByFilterGenerator == null) {
                throw new IncorrectArgumentInputException(this, ctx, ctx.getInput(name));
            }
            throw inputExceptionByFilterGenerator.apply(ctx);
        }

        if (shaper == null) {
            return t;
        }
        return shaper.apply(t);
    }

    public abstract T parse(CommandContext ctx) throws CommandSyntaxException, IncorrectArgumentInputException;

    @Accessors(chain = true, fluent = true)
    @Setter
    public static class Option<T> {
        protected SuggestionAction suggestionAction;
        protected SuggestionAction additionalSuggestionAction;
        protected Predicate<? super T> filter;
        protected Function<? super T, ? extends T> shaper;
        protected ContextAction contextAction;

        protected Optional<SuggestionAction> suggestionAction() {
            return Optional.ofNullable(suggestionAction);
        }

        protected Optional<SuggestionAction> additionalSuggestionAction() {
            return Optional.ofNullable(additionalSuggestionAction);
        }

        protected Optional<Predicate<? super T>> filter() {
            return Optional.ofNullable(filter);
        }

        protected Optional<Function<? super T, ? extends T>> shaper() {
            return Optional.ofNullable(shaper);
        }

        protected Optional<ContextAction> contextAction() {
            return Optional.ofNullable(contextAction);
        }
    }
}
