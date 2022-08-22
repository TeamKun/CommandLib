package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    void setContextAction(ContextAction contextAction) {
        this.contextAction = contextAction;
    }

    boolean hasContextAction() {
        return contextAction != null;
    }

    final RequiredArgumentBuilder<CommandSource, ?> toBuilder(Command parent, ArgumentsParser argsParser) {
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
            List<Object> parsedArgs = new ArrayList<>();
            Map<String, Object> parsedArgMap = new HashMap<>();
            try {
                argsParser.parse(parsedArgs, parsedArgMap, ctx);
            } catch (IncorrectArgumentInputException e) {
                e.sendMessage(ctx.getSource());
                return 1;
            }
            return CommandLib.executeWithStackTrace(new net.kunmc.lab.commandlib.CommandContext(parent, ctx, parsedArgs, parsedArgMap), contextAction);
        });

        return builder;
    }

    String generateHelpMessageTag() {
        return String.format(TextFormatting.GRAY + "<" + TextFormatting.YELLOW + "%s" + TextFormatting.GRAY + ">", name);
    }

    public abstract T parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException;
}
