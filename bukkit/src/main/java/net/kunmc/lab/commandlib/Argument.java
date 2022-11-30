package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Argument<T> {
    protected final String name;
    private SuggestionAction suggestionAction;
    private SuggestionAction additionalSuggestionAction;
    private ContextAction contextAction;
    private final ArgumentType<?> type;
    private Predicate<? super T> filter;
    private Function<? super T, ? extends T> shaper;
    private Function<CommandContext<CommandListenerWrapper>, IncorrectArgumentInputException> inputExceptionByFilterGenerator;

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

    public ArgumentType<?> type() {
        return type;
    }

    public abstract T cast(Object parsedArgument);

    protected void setContextAction(ContextAction contextAction) {
        this.contextAction = contextAction;
    }

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

    protected void setInputExceptionByFilterGenerator(Function<CommandContext<CommandListenerWrapper>, IncorrectArgumentInputException> inputExceptionByFilterGenerator) {
        this.inputExceptionByFilterGenerator = inputExceptionByFilterGenerator;
    }

    String generateHelpMessageTag() {
        return String.format(ChatColor.GRAY + "<" + ChatColor.YELLOW + "%s" + ChatColor.GRAY + ">", name);
    }

    protected static IncorrectArgumentInputException convertSyntaxException(CommandSyntaxException e) {
        ChatMessage msg = ((ChatMessage) e.getRawMessage());
        TranslatableComponent component = Component.translatable()
                                                   .key((msg.getKey()))
                                                   .args(Arrays.stream(msg.getArgs())
                                                               .map(Object::toString)
                                                               .map(Component::text)
                                                               .collect(Collectors.toList()))
                                                   .color(TextColor.color(ChatColor.RED.asBungee()
                                                                                       .getColor()
                                                                                       .getRGB()))
                                                   .build();

        return new IncorrectArgumentInputException(component);
    }

    protected static String getInputString(CommandContext<CommandListenerWrapper> ctx, String name) {
        try {
            Field f = ctx.getClass()
                         .getDeclaredField("arguments");
            f.setAccessible(true);
            ParsedArgument<CommandListenerWrapper, ?> argument = ((Map<String, ParsedArgument<CommandListenerWrapper, ?>>) f.get(
                    ctx)).get(name);
            if (argument == null) {
                return "";
            }

            return argument.getRange()
                           .get(ctx.getInput());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    final T parseInternal(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        T t;
        try {
            t = parse(ctx);
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }

        if (filter != null && !filter.test(t)) {
            if (inputExceptionByFilterGenerator == null) {
                throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
            }
            throw inputExceptionByFilterGenerator.apply(ctx);
        }

        if (shaper == null) {
            return t;
        }
        return shaper.apply(t);
    }

    public abstract T parse(CommandContext<CommandListenerWrapper> ctx) throws CommandSyntaxException, IncorrectArgumentInputException;

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
