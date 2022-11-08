package net.kunmc.lab.commandlib;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CommandContext {
    private final Command command;
    private final com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> handle;
    private final List<String> args;
    private final CommandSender sender;
    private final List<Object> parsedArgList;
    private final Map<String, Object> parsedArgMap;

    public CommandContext(Command command,
                          com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> ctx,
                          List<Object> parsedArgList,
                          Map<String, Object> parsedArgMap) {
        this.command = command;
        this.handle = ctx;
        // TODO 明らかに間違ったロジックなので直すか消すかしたい
        this.args = ImmutableList.copyOf(ctx.getInput()
                                            .replaceFirst("^/", "")
                                            .split(" "));
        this.sender = ctx.getSource()
                         .getBukkitSender();
        this.parsedArgList = parsedArgList;
        this.parsedArgMap = parsedArgMap;
    }

    public com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> getHandle() {
        return handle;
    }

    @Deprecated
    public List<String> getArgs() {
        return args;
    }

    @Deprecated
    public String getArg(int index) {
        return args.get(index);
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
        return sender;
    }

    public void sendHelp() {
        command.sendHelp(this);
    }

    public void sendMessage(@Nullable Object obj) {
        sendMessage(Objects.toString(obj));
    }

    public void sendMessage(@Nullable String message) {
        sender.sendMessage(Objects.toString((message)));
    }

    public void sendMessage(@NotNull Component component) {
        sender.sendMessage(component);
    }

    public void sendSuccess(@Nullable Object obj) {
        sendSuccess(Objects.toString(obj));
    }

    public void sendSuccess(@Nullable String message) {
        sendMessage(ChatColor.GREEN + message);
    }

    public void sendSuccess(@NotNull Component component) {
        sendMessage(component.color(TextColor.color(ChatColor.GREEN.asBungee()
                                                                   .getColor()
                                                                   .getRGB())));
    }

    public void sendWarn(@Nullable Object obj) {
        sendWarn(Objects.toString(obj));
    }

    public void sendWarn(@Nullable String message) {
        sendMessage(ChatColor.YELLOW + message);
    }

    public void sendWarn(@NotNull Component component) {
        sendMessage(component.color(TextColor.color(ChatColor.YELLOW.asBungee()
                                                                    .getColor()
                                                                    .getRGB())));
    }

    public void sendFailure(@Nullable Object obj) {
        sendFailure(Objects.toString(obj));
    }

    public void sendFailure(@Nullable String message) {
        sendMessage(ChatColor.RED + message);
    }

    public void sendFailure(@NotNull Component component) {
        sendMessage(component.color(TextColor.color(ChatColor.RED.asBungee()
                                                                 .getColor()
                                                                 .getRGB())));
    }
}
