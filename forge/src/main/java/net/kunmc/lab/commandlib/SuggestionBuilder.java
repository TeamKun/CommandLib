package net.kunmc.lab.commandlib;

import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SuggestionBuilder {
    private final List<Suggestion> suggestions = new ArrayList<>();
    private final CommandContext ctx;

    public SuggestionBuilder(CommandContext ctx) {
        this.ctx = ctx;
    }

    public com.mojang.brigadier.context.CommandContext<CommandSource> getHandle() {
        return ctx.getHandle();
    }

    public CommandContext getContext() {
        return ctx;
    }

    public String getInput() {
        return ctx.getHandle()
                  .getInput();
    }

    public String getLatestInput() {
        String input = getInput();
        if (!isWaitingQuote() && input.endsWith(" ")) {
            return "";
        }

        List<ParsedCommandNode<CommandSource>> nodes = ctx.getHandle()
                                                          .getNodes();
        if (nodes.size() == 0) {
            return "";
        }

        StringRange range = nodes.get(nodes.size() - 1)
                                 .getRange();
        if (input.length() == range.getEnd()) {
            return range.get(input);

        }
        return input.substring(range.getEnd() + 1);
    }

    private boolean isWaitingQuote() {
        int count = 0;
        String input = getInput();
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == '"' && input.charAt(i - 1) != '\\') {
                count++;
            }
        }
        return count % 2 != 0;
    }

    public List<String> getArgs() {
        return ctx.getArgs();
    }

    public String getArg(int index) {
        return ctx.getArg(index);
    }

    public String getInput(String name) {
        return ctx.getInput(name);
    }

    public List<Object> getParsedArgs() {
        return ctx.getParsedArgs();
    }

    public Object getParsedArg(int index) {
        return ctx.getParsedArg(index);
    }

    public Object getParsedArg(String name) {
        return ctx.getParsedArg(name);
    }

    public <T> T getParsedArg(int index, Class<T> clazz) {
        return ctx.getParsedArg(index, clazz);
    }

    public <T> T getParsedArg(String name, Class<T> clazz) {
        return ctx.getParsedArg(name, clazz);
    }

    public CommandSource getSender() {
        return ctx.getSender();
    }

    public void sendMessage(String message) {
        ctx.sendMessage(message);
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
