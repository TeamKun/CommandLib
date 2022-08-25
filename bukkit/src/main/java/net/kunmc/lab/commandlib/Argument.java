package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;

public abstract class Argument<T> {
    protected final String name;
    private final SuggestionAction suggestionAction;
    private ContextAction contextAction;
    private final ArgumentType<?> type;

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

    public ContextAction contextAction() {
        return contextAction;
    }

    public ArgumentType<?> type() {
        return type;
    }

    void setContextAction(ContextAction contextAction) {
        this.contextAction = contextAction;
    }

    boolean hasContextAction() {
        return contextAction != null;
    }

    String generateHelpMessageTag() {
        return String.format(ChatColor.GRAY + "<" + ChatColor.YELLOW + "%s" + ChatColor.GRAY + ">", name);
    }

    public abstract T parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException;
}
