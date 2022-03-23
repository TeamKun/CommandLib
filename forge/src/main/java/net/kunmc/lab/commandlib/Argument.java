package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.function.Function;

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

    void setContextAction(ContextAction contextAction) {
        this.contextAction = contextAction;
    }

    final RequiredArgumentBuilder<CommandSource, ?> toBuilder(Command parent, Function<CommandContext<CommandSource>, List<Object>> argsParser) {
        RequiredArgumentBuilder<CommandSource, ?> builder = RequiredArgumentBuilder.argument(name, type);

        if (suggestionAction != null) {
            builder.suggests((ctx, sb) -> {
                SuggestionBuilder suggestionBuilder = new SuggestionBuilder(ctx, argsParser);
                suggestionAction.accept(suggestionBuilder);
                suggestionBuilder.build().forEach(s -> {
                    s.suggest(sb);
                });

                return sb.buildFuture();
            });
        }

        if (contextAction == null) {
            contextAction = parent::execute;
        }
        builder.executes(ctx -> {
            return CommandLib.executeWithStackTrace(new net.kunmc.lab.commandlib.CommandContext(parent, ctx, argsParser.apply(ctx)), contextAction);
        });

        return builder;
    }

    String generateHelpMessageTag() {
        return String.format(TextFormatting.GRAY + "<" + TextFormatting.YELLOW + "%s" + TextFormatting.GRAY + ">", name);
    }

    public abstract T parse(CommandContext<CommandSource> ctx);
}
