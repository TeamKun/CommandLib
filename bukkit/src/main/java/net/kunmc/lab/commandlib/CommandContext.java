package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandListenerWrapper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class CommandContext extends AbstractCommandContext<Object, BaseComponent> {
    public CommandContext(com.mojang.brigadier.context.CommandContext<Object> ctx) {
        super(ctx);
    }

    public Entity getEntity() {
        return NMSCommandListenerWrapper.create(handle.getSource())
                                        .getBukkitEntity();
    }

    public World getWorld() {
        return NMSCommandListenerWrapper.create(handle.getSource())
                                        .getBukkitWorld();
    }

    public Location getLocation() {
        return NMSCommandListenerWrapper.create(handle.getSource())
                                        .getBukkitLocation();
    }

    public CommandSender getSender() {
        return NMSCommandListenerWrapper.create(handle.getSource())
                                        .getBukkitSender();
    }

    @Override
    public void sendMessage(@Nullable String message) {
        sendMessage(new TextComponent(String.valueOf(message)));
    }

    public void sendMessage(@NotNull BaseComponent component) {
        getSender().spigot()
                   .sendMessage(Objects.requireNonNull(component));
    }

    @Override
    public void sendSuccess(@Nullable String message) {
        sendSuccess(new TextComponent(message));
    }

    public void sendSuccess(@NotNull BaseComponent component) {
        component.setColor(ChatColor.GREEN);
        sendMessage(component);
    }

    @Override
    public void sendWarn(@Nullable String message) {
        sendWarn(new TextComponent(String.valueOf(message)));
    }

    public void sendWarn(@NotNull BaseComponent component) {
        component.setColor(ChatColor.YELLOW);
        sendMessage(component);
    }

    @Override
    public void sendFailure(@Nullable String message) {
        sendFailure(new TextComponent(String.valueOf(message)));
    }

    public void sendFailure(@NotNull BaseComponent component) {
        component.setColor(ChatColor.RED);
        sendMessage(component);
    }

    @Override
    public void sendComponent(BaseComponent component) {
        sendMessage(component);
    }
}
