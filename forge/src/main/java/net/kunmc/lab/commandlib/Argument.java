package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public abstract class Argument<T> {
    protected final String name;
    private final ArgumentType<?> type;
    private SuggestionAction suggestionAction;
    private ContextAction contextAction;

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

    String generateHelpMessageTag() {
        return String.format(TextFormatting.GRAY + "<" + TextFormatting.YELLOW + "%s" + TextFormatting.GRAY + ">", name);
    }

    protected static IncorrectArgumentInputException convertSyntaxException(CommandSyntaxException e) {
        return new IncorrectArgumentInputException(((ITextComponent) e.getRawMessage()));
    }

    public abstract T parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException;
}
