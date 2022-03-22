package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class CommandContext {
    private final Command command;
    private final com.mojang.brigadier.context.CommandContext<CommandSource> handle;
    private final List<String> args;
    private final CommandSource sender;
    private final List<Object> parsedArgs;

    public CommandContext(Command command, com.mojang.brigadier.context.CommandContext<CommandSource> ctx, List<Object> parsedArgs) {
        this.command = command;
        this.handle = ctx;
        this.args = ImmutableList.copyOf(ctx.getInput().replaceFirst("^/", "").split(" "));
        this.sender = ctx.getSource();
        this.parsedArgs = parsedArgs;
    }

    public com.mojang.brigadier.context.CommandContext<CommandSource> getHandle() {
        return handle;
    }

    public List<String> getArgs() {
        return args;
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
        return sender;
    }

    public void sendHelp() {
        command.sendHelp(handle);
    }

    public void sendMessage(String message) {
        sendMessage(message, false);
    }

    public void sendMessage(String message, boolean allowLogging) {
        sender.sendFeedback(new StringTextComponent(message), allowLogging);
    }

    public void sendSuccess(String message) {
        sendSuccess(message, false);
    }

    public void sendSuccess(String message, boolean allowLogging) {
        sendMessage(TextFormatting.GREEN + message, allowLogging);
    }

    public void sendWarn(String message) {
        sendWarn(message, false);
    }

    public void sendWarn(String message, boolean allowLogging) {
        sendMessage(TextFormatting.YELLOW + message, allowLogging);
    }

    public void sendFailure(String message) {
        sendFailure(message, false);
    }

    public void sendFailure(String message, boolean allowLogging) {
        sendMessage(TextFormatting.RED + message, allowLogging);
    }
}
