package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.util.TextColorUtil;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandListenerWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public final class CommandContext extends AbstractCommandContext<Object, Component> {
    public CommandContext(com.mojang.brigadier.context.CommandContext<Object> ctx) {
        super(ctx);
    }

    public Entity getEntity() {
        return new NMSCommandListenerWrapper(handle.getSource()).getBukkitEntity();
    }

    public World getWorld() {
        return new NMSCommandListenerWrapper(handle.getSource()).getBukkitWorld();
    }

    public Location getLocation() {
        return new NMSCommandListenerWrapper(handle.getSource()).getBukkitLocation();
    }

    public CommandSender getSender() {
        return new NMSCommandListenerWrapper(handle.getSource()).getBukkitSender();
    }

    @Override
    public void sendMessage(@Nullable String message) {
        sendMessage(Component.text(String.valueOf(message)));
    }

    public void sendMessage(@NotNull Component component) {
        getSender().sendMessage(Objects.requireNonNull(component));
    }

    @Override
    public void sendMessageWithOption(@Nullable String message, @NotNull Consumer<MessageOption> options) {
        Component messageComponent = MessageOption.createMessage(options,
                                                                 (rgb, hoverText) -> Component.text(String.valueOf(
                                                                                                      message))
                                                                                              .hoverEvent(HoverEvent.showText(
                                                                                                      Component.text(
                                                                                                              hoverText)))
                                                                                              .color(TextColor.color(rgb)));

        sendMessage(messageComponent);
    }

    @Override
    public void sendSuccess(@Nullable String message) {
        sendSuccess(Component.text(String.valueOf(message)));
    }

    public void sendSuccess(@NotNull Component component) {
        sendMessage(component.color(TextColorUtil.GREEN));
    }

    @Override
    public void sendWarn(@Nullable String message) {
        sendWarn(Component.text(String.valueOf(message)));
    }

    public void sendWarn(@NotNull Component component) {
        sendMessage(component.color(TextColorUtil.YELLOW));
    }

    @Override
    public void sendFailure(@Nullable String message) {
        sendFailure(Component.text(String.valueOf(message)));
    }

    public void sendFailure(@NotNull Component component) {
        sendMessage(component.color(TextColorUtil.RED));
    }

    @Override
    public void sendComponent(Component component) {
        sendMessage(component);
    }
}
