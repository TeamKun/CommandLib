package net.kunmc.lab.commandlib.suggestion;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SuggestionBuilder<C extends AbstractCommandContext<?, ?>> {
    private final List<Suggestion> suggestions = new ArrayList<>();
    private final C ctx;

    public SuggestionBuilder(@NotNull C ctx) {
        this.ctx = Objects.requireNonNull(ctx);
    }

    @NotNull
    public C getContext() {
        return ctx;
    }

    public String getInput() {
        return ctx.getHandle()
                  .getInput();
    }

    @NotNull
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

    public List<String> getInputs() {
        return ctx.getInputs();
    }

    public String getInput(int index) {
        return ctx.getInput(index);
    }

    public String getInput(String name) {
        return ctx.getInput(name);
    }

    public List<Object> getArguments() {
        return ctx.getArguments();
    }

    public Object getArgument(int index) {
        return ctx.getArgument(index);
    }

    public Object getArgument(String name) {
        return ctx.getArgument(name);
    }

    public <T> T getArgument(int index, Class<T> clazz) {
        return ctx.getArgument(index, clazz);
    }

    public <T> T getArgument(String name, Class<T> clazz) {
        return ctx.getArgument(name, clazz);
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

    public List<Suggestion> build() {
        return suggestions;
    }
}
