package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandContext {
    private final Command command;
    private final com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> handle;
    private final List<String> args;
    private final CommandSender sender;
    private final List<Object> parsedArgs;

    public CommandContext(Command command, com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> ctx, List<Object> parsedArgs) {
        this.command = command;
        this.handle = ctx;
        this.args = ImmutableList.copyOf(ctx.getInput().replaceFirst("^/", "").split(" "));
        this.sender = ctx.getSource().getBukkitSender();
        this.parsedArgs = parsedArgs;
    }

    public com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> getHandle() {
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

    public CommandSender getSender() {
        return sender;
    }

    public void sendHelp() {
        command.sendHelp(handle);
    }

    public void sendMessage(String message) {
        sender.sendMessage(message);
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
}
