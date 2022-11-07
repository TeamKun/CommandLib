package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CommandContext {
    private final Command command;
    private final com.mojang.brigadier.context.CommandContext<CommandSource> handle;
    private final List<String> args;
    private final CommandSource sender;
    private final List<Object> parsedArgList;
    private final Map<String, Object> parsedArgMap;

    public CommandContext(Command command, com.mojang.brigadier.context.CommandContext<CommandSource> ctx, List<Object> parsedArgList, Map<String, Object> parsedArgMap) {
        this.command = command;
        this.handle = ctx;
        // TODO 明らかに間違ったロジックなので直すか消すかしたい
        this.args = ImmutableList.copyOf(ctx.getInput().replaceFirst("^/", "").split(" "));
        this.sender = ctx.getSource();
        this.parsedArgList = parsedArgList;
        this.parsedArgMap = parsedArgMap;
    }

    public com.mojang.brigadier.context.CommandContext<CommandSource> getHandle() {
        return handle;
    }

    @Deprecated
    public List<String> getArgs() {
        return args;
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
        return sender;
    }

    public void sendHelp() {
        command.sendHelp(this);
    }

    public void sendMessage(@Nullable Object obj) {
        sendMessage(Objects.toString(obj));
    }

    public void sendMessage(String message) {
        sendMessage(message, false);
    }

    public void sendMessage(String message, boolean allowLogging) {
        sender.sendFeedback(new StringTextComponent(message), allowLogging);
    }

    public void sendMessage(ITextComponent component) {
        sendMessage(component, false);
    }

    public void sendMessage(ITextComponent component, boolean allowLogging) {
        sender.sendFeedback(component, allowLogging);
    }

    public void sendSuccess(@Nullable Object obj) {
        sendSuccess(Objects.toString(obj));
    }

    public void sendSuccess(String message) {
        sendSuccess(message, false);
    }

    public void sendSuccess(String message, boolean allowLogging) {
        sendMessage(TextFormatting.GREEN + message, allowLogging);
    }

    public void sendWarn(@Nullable Object obj) {
        sendWarn(Objects.toString(obj));
    }

    public void sendWarn(String message) {
        sendWarn(message, false);
    }

    public void sendWarn(String message, boolean allowLogging) {
        sendMessage(TextFormatting.YELLOW + message, allowLogging);
    }

    public void sendFailure(@Nullable Object obj) {
        sendFailure(Objects.toString(obj));
    }

    public void sendFailure(String message) {
        sendFailure(message, false);
    }

    public void sendFailure(String message, boolean allowLogging) {
        sendMessage(TextFormatting.RED + message, allowLogging);
    }
}
