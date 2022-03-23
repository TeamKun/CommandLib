package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SuggestionBuilder {
    private final List<Suggestion> suggestions = new ArrayList<>();
    private final CommandContext<CommandListenerWrapper> ctx;
    private final List<Object> parsedArgs;

    public SuggestionBuilder(CommandContext<CommandListenerWrapper> ctx, Function<CommandContext<CommandListenerWrapper>, List<Object>> argsParser) {
        this.ctx = ctx;
        this.parsedArgs = argsParser.apply(ctx);
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
