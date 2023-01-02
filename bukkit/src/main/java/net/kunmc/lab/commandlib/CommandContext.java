package net.kunmc.lab.commandlib;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class CommandContext {
    private final com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> handle;
    private final Map<String, String> argumentNameToInputArgMap = new LinkedHashMap<>();
    private final CommandSender sender;
    private final LinkedHashMap<String, Object> parsedArgMap = new LinkedHashMap<>();

    public CommandContext(com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> ctx) {
        this.handle = ctx;
        this.sender = ctx.getSource()
                         .getBukkitSender();

        ctx.getNodes()
           .forEach(x -> {
               argumentNameToInputArgMap.put(x.getNode()
                                              .getName(),
                                             x.getRange()
                                              .get(ctx.getInput()));
           });
    }

    public com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> getHandle() {
        return handle;
    }

    public String getInput(String name) {
        return argumentNameToInputArgMap.getOrDefault(name, "");
    }

    public String getArg(int index) {
        return getArgs().get(index);
    }

    public List<String> getArgs() {
        return new ArrayList<>(argumentNameToInputArgMap.values());
    }

    public List<Object> getParsedArgs() {
        return Arrays.asList(parsedArgMap.values()
                                         .toArray());
    }

    public Object getParsedArg(int index) {
        return getParsedArgs().get(index);
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

    public Entity getEntity() {
        return handle.getSource()
                     .getBukkitEntity();
    }

    public World getWorld() {
        return handle.getSource()
                     .getBukkitWorld();
    }

    public Location getLocation() {
        return handle.getSource()
                     .getBukkitLocation();
    }

    public CommandSender getSender() {
        return sender;
    }

    public void sendMessage(@Nullable Object obj) {
        sendMessage(Objects.toString(obj));
    }

    public void sendMessage(@Nullable String message) {
        sender.sendMessage(String.valueOf(message));
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

    void addParsedArgument(String name, Object parsedArgument) {
        parsedArgMap.put(name, parsedArgument);
    }
}
