package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuggestionBuilder {
    private final List<Suggestion> suggestions = new ArrayList<>();
    private final CommandContext<CommandListenerWrapper> ctx;
    private final List<Object> parsedArgList;
    private final Map<String, Object> parsedArgMap;

    public SuggestionBuilder(CommandContext<CommandListenerWrapper> ctx, List<Object> parsedArgList, Map<String, Object> parsedArgMap) {
        this.ctx = ctx;
        this.parsedArgList = parsedArgList;
        this.parsedArgMap = parsedArgMap;
    }

    public CommandContext<CommandListenerWrapper> getHandle() {
        return ctx;
    }

    public String getInput() {
        return ctx.getInput();
    }

    public String getLatestInput() {
        if (!isWaitingQuote() && getInput().endsWith(" ")) {
            return "";
        }

        List<ParsedCommandNode<CommandListenerWrapper>> nodes = ctx.getNodes();
        if (nodes.size() == 0) {
            return "";
        }
        ParsedCommandNode<CommandListenerWrapper> last = nodes.get(nodes.size() - 1);
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

    @Deprecated
    public List<String> getArgs() {
        // TODO 明らかに間違ったロジックなので直すか消すかしたい
        List<String> list = Lists.newArrayList(ctx.getInput().replaceFirst("^/", "").split(" "));
        if (ctx.getInput().endsWith(" ")) {
            list.add("");
        }

        return ImmutableList.copyOf(list);
    }

    public String getArg(int index) {
        return getArgs().get(index);
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

    public CommandSender getSender() {
        return ctx.getSource().getBukkitSender();
    }

    public void sendMessage(String message) {
        getSender().sendMessage(message);
    }

    public void sendSuccess(String message) {
        sendMessage(ChatColor.GREEN + message);
    }

    public void sendWarn(String message) {
        sendMessage(ChatColor.YELLOW + message);
    }

    public void sendFailure(String message) {
        sendMessage(ChatColor.RED + message);
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
