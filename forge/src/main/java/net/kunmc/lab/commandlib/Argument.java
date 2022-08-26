package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Argument<T> {
    protected final String name;
    private final ArgumentType<?> type;
    private SuggestionAction suggestionAction;
    private ContextAction contextAction;
    private Predicate<? super T> filter;
    private Function<? super T, ? extends T> shaper;
    private Function<CommandContext<CommandSource>, IncorrectArgumentInputException> inputExceptionByFilterGenerator;

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
        return suggestionAction;
    }

    protected void setSuggestionAction(SuggestionAction suggestionAction) {
        this.suggestionAction = suggestionAction;
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

    boolean hasContextAction() {
        return contextAction != null;
    }

    protected void setFilter(Predicate<? super T> filter) {
        this.filter = filter;
    }

    protected void setSharper(Function<? super T, ? extends T> shaper) {
        this.shaper = shaper;
    }

    protected void setInputExceptionByFilterGenerator(Function<CommandContext<CommandSource>, IncorrectArgumentInputException> inputExceptionByFilterGenerator) {
        this.inputExceptionByFilterGenerator = inputExceptionByFilterGenerator;
    }

    String generateHelpMessageTag() {
        return String.format(TextFormatting.GRAY + "<" + TextFormatting.YELLOW + "%s" + TextFormatting.GRAY + ">", name);
    }

    protected static IncorrectArgumentInputException convertSyntaxException(CommandSyntaxException e) {
        return new IncorrectArgumentInputException(((ITextComponent) e.getRawMessage()));
    }

    protected static String getInputString(CommandContext<CommandSource> ctx, String name) {
        try {
            Field f = ctx.getClass().getDeclaredField("arguments");
            f.setAccessible(true);
            ParsedArgument<CommandSource, ?> argument = ((Map<String, ParsedArgument<CommandSource, ?>>) f.get(ctx)).get(name);
            return argument.getRange().get(ctx.getInput());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    final T parseInternal(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
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

    public abstract T parse(CommandContext<CommandSource> ctx) throws CommandSyntaxException, IncorrectArgumentInputException;
}
