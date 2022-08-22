package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestionBuilder {
    private final List<Suggestion> suggestions = new ArrayList<>();
    private final List<Object> parsedArgList = new ArrayList<>();
    private final Map<String, Object> parsedArgMap = new HashMap<>();
    private final CommandContext<CommandSource> ctx;

    public SuggestionBuilder(CommandContext<CommandSource> ctx, ArgumentsParser argsParser) {
        this.ctx = ctx;

        try {
            argsParser.parse(parsedArgList, parsedArgMap, ctx);
        } catch (IncorrectArgumentInputException ignored) {
        }
    }

    public String getInput() {
        return ctx.getInput();
    }

    public String getLatestInput() {
        if (!isWaitingQuote() && getInput().endsWith(" ")) {
            return "";
        }

        List<ParsedCommandNode<CommandSource>> nodes = ctx.getNodes();
        if (nodes.size() == 0) {
            return "";
        }
        ParsedCommandNode<CommandSource> last = nodes.get(nodes.size() - 1);
        return last.getRange().get(getInput());
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
        // TODO 明らかに間違ったロジックなので直すか消すかしたい
        List<String> list = Lists.newArrayList(ctx.getInput().replaceFirst("^/", "").split(" "));
        if (ctx.getInput().endsWith(" ")) {
            list.add("");
        }

        return ImmutableList.copyOf(list);
    }

    public List<Object> getParsedArgs() {
        return parsedArgList;
    }

    public Object getParsedArg(int index) {
        return parsedArgList.get(index);
    }

    public Object getParsedArg(String name) {
        return parsedArgMap.get(name);
    }

    public <T> T getParsedArg(int index, Class<T> clazz) {
        Object parsedArg = getParsedArg(index);
        if (parsedArg == null) {
            return null;
        }

        return clazz.cast(parsedArg);
    }

    public <T> T getParsedArg(String name, Class<T> clazz) {
        Object parsedArg = getParsedArg(name);
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
