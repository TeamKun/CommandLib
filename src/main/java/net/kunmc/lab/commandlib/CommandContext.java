package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class CommandContext {
    private final List<String> args;
    private final CommandSource sender;
    private final List<Object> parsedArgs;

    public CommandContext(CommandSource sender, String input, List<Object> parsedArgs) {
        this.args = ImmutableList.copyOf(input.replaceFirst("^/", "").split(" "));
        this.sender = sender;
        this.parsedArgs = ImmutableList.copyOf(parsedArgs);
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
        return clazz.cast(parsedArgs.get(index));
    }

    public CommandSource getSender() {
        return sender;
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
