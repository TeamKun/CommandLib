package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestionBuilder {
    private final List<Suggestion> suggestions = new ArrayList<>();
    private final CommandContext<CommandListenerWrapper> ctx;
    private final List<Object> parsedArgList = new ArrayList<>();
    private final Map<String, Object> parsedArgMap = new HashMap<>();

    public SuggestionBuilder(CommandContext<CommandListenerWrapper> ctx, ArgumentsParser argsParser) {
        this.ctx = ctx;

        try {
            argsParser.parse(parsedArgList, parsedArgMap, ctx);
        } catch (IncorrectArgumentInputException ignored) {
        }
    }

    public List<String> getArgs() {
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
