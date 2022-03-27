package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SuggestionBuilder {
    private final List<Suggestion> suggestions = new ArrayList<>();
    private final List<Object> parsedArgs = new ArrayList<>();
    private final CommandContext<CommandSource> ctx;

    public SuggestionBuilder(CommandContext<CommandSource> ctx, ArgumentsParser argsParser) {
        this.ctx = ctx;

        try {
            argsParser.parse(parsedArgs, ctx);
        } catch (IncorrectArgumentInputException ignored) {
        }
    }

    public List<String> getArgs() {
        return ImmutableList.copyOf(ctx.getInput().replaceFirst("^/", "").split(" "));
    }

    public List<Object> getParsedArgs() {
        return parsedArgs;
    }

    public Object getParsedArg(int index) {
        return parsedArgs.get(index);
    }

    public <T> T getParsedArg(int index, Class<T> clazz) {
        Object parsedArg = getParsedArg(index);
        if (parsedArg == null) {
            return null;
        }

        return clazz.cast(parsedArg);
    }

    public CommandSource getSender() {
        return ctx.getSource();
    }

    public void sendMessage(String message) {
        getSender().sendFeedback(new StringTextComponent(message), false);
    }

    public void sendSuccess(String message) {
        sendMessage(TextFormatting.GREEN + message);
    }

    public void sendWarn(String message) {
        sendMessage(TextFormatting.YELLOW + message);
    }

    public void sendFailure(String message) {
        sendMessage(TextFormatting.RED + message);
    }

    public SuggestionBuilder suggest(@NotNull String suggest) {
        suggestions.add(new Suggestion(suggest, null));
        return this;
    }

    public SuggestionBuilder suggest(@NotNull String suggest, @Nullable String tooltip) {
        suggestions.add(new Suggestion(suggest, tooltip));
        return this;
    }

    List<Suggestion> build() {
        return suggestions;
    }
}
