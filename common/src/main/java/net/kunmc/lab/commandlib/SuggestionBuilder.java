package net.kunmc.lab.commandlib;

import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SuggestionBuilder<C extends AbstractCommandContext<?, ?>> {
    private final List<Suggestion> suggestions = new ArrayList<>();
    private final C ctx;

    public SuggestionBuilder(C ctx) {
        this.ctx = ctx;
    }

    public C getContext() {
        return ctx;
    }

    public String getInput() {
        return ctx.getHandle()
                  .getInput();
    }

    public String getLatestInput() {
        String input = getInput();

        List<? extends ParsedCommandNode<?>> nodes = ctx.getHandle()
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

    public void sendMessage(String message) {
        ctx.sendMessage(message);
    }

    public void sendSuccess(String message) {
        ctx.sendSuccess(message);
    }

    public void sendWarn(String message) {
        ctx.sendWarn(message);
    }

    public void sendFailure(String message) {
        ctx.sendFailure(message);
    }

    public SuggestionBuilder<C> suggest(@NotNull String suggest) {
        return suggest(suggest, null);
    }

    public SuggestionBuilder<C> suggest(@NotNull String suggest, @Nullable String tooltip) {
        suggestions.add(new Suggestion(suggest, tooltip));
        return this;
    }

    List<Suggestion> build() {
        return suggestions;
    }
}
