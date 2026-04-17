package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import net.kunmc.lab.commandlib.command.CommandExecutor;
import net.kunmc.lab.commandlib.suggestion.SuggestionAction;

public abstract class Argument<T> extends CommonArgument<T, CommandContext> {
    public Argument(String name, ArgumentType<?> type) {
        super(name, type);
    }

    public Argument(String name,
                    SuggestionAction<CommandContext> suggestionAction,
                    CommandExecutor<CommandContext> executor,
                    ArgumentType<?> type) {
        super(name, suggestionAction, executor, type);
    }
}
