package net.kunmc.lab.commandlib;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;

import java.util.List;
import java.util.function.Function;

public abstract class Argument<T> {
    protected final String name;
    private final SuggestionAction suggestionAction;
    private final ContextAction contextAction;
    private final ArgumentType<?> type;

    public Argument(String name, SuggestionAction suggestionAction, ContextAction contextAction, ArgumentType<?> type) {
        this.name = name;
        this.suggestionAction = suggestionAction;
        this.contextAction = contextAction;
        this.type = type;
    }

    final RequiredArgumentBuilder<CommandSource, ?> toBuilder(Function<CommandContext<CommandSource>, List<Object>> argsParser) {
        RequiredArgumentBuilder<CommandSource, ?> builder = RequiredArgumentBuilder.argument(name, type);

        builder.suggests((ctx, sb) -> {
            if (suggestionAction != null) {
                SuggestionBuilder suggestionBuilder = new SuggestionBuilder();
                suggestionAction.accept(suggestionBuilder);

                suggestionBuilder.build().forEach(s -> {
                    if (s.tooltip != null) {
                        sb.suggest(s.text, new LiteralMessage(s.tooltip));
                    } else {
                        sb.suggest(s.text);
                    }
                });

                return sb.buildFuture();
            } else {
                return type.listSuggestions(ctx, sb);
            }
        });

        builder.executes(ctx -> {
            if (contextAction == null) {
                return 0;
            }

            contextAction.accept(new net.kunmc.lab.commandlib.CommandContext(ctx.getSource(), ctx.getInput(), argsParser.apply(ctx)));
            return 1;
        });

        return builder;
    }

    public abstract T parse(CommandContext<CommandSource> ctx);
}
