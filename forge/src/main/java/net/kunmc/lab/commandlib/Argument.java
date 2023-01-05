package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;

public abstract class Argument<T> extends CommonArgument<T, CommandContext> {
    public Argument(String name, ArgumentType<?> type) {
        super(name, type);
    }

    public Argument(String name,
                    SuggestionAction<CommandContext> suggestionAction,
                    ContextAction<CommandContext> contextAction,
                    ArgumentType<?> type) {
        super(name, suggestionAction, contextAction, type);
    }
}
