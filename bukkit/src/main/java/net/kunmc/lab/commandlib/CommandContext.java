package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.TextColorUtil;
import net.kunmc.lab.commandlib.util.text.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class CommandContext extends AbstractCommandContext<CommandListenerWrapper, Component> {
    public CommandContext(com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> ctx) {
        super(ctx);
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
        return handle.getSource()
                     .getBukkitSender();
    }

    public void sendMessage(@Nullable String message) {
        sendMessage(Component.text(String.valueOf(message)));
    }

    public void sendMessage(@NotNull Component component) {
        getSender().sendMessage(component);
    }

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

    public void sendSuccess(@Nullable String message) {
        sendSuccess(Component.text(String.valueOf(message)));
    }

    public void sendSuccess(@NotNull Component component) {
        sendMessage(component.color(TextColorUtil.GREEN));
    }

    public void sendWarn(@Nullable String message) {
        sendWarn(Component.text(String.valueOf(message)));
    }

    public void sendWarn(@NotNull Component component) {
        sendMessage(component.color(TextColorUtil.YELLOW));
    }

    public void sendFailure(@Nullable String message) {
        sendFailure(Component.text(String.valueOf(message)));
    }

    public void sendFailure(@NotNull Component component) {
        sendMessage(component.color(TextColorUtil.RED));
    }

    @Override
    public void sendComponentBuilder(ComponentBuilder<? extends Component, ?> builder) {
        sendMessage(builder.build());
    }

    @Override
    public TextComponentBuilder<? extends Component, ?> createTextComponentBuilder(@NotNull String text) {
        return new TextComponentBuilderImpl(text);
    }

    @Override
    public TranslatableComponentBuilder<? extends Component, ?> createTranslatableComponentBuilder(@NotNull String key) {
        return new TranslatableComponentBuilderImpl(key);
    }

    @Override
    IncorrectArgumentInputException convertCommandSyntaxException(CommandSyntaxException e) {
        ChatMessage msg = ((ChatMessage) e.getRawMessage());
        return new IncorrectArgumentInputException(ctx -> {
            ((CommandContext) ctx).sendFailure(Component.translatable(msg.getKey())
                                                        .args(Arrays.stream(msg.getArgs())
                                                                    .map(String::valueOf)
                                                                    .map(Component::text)
                                                                    .collect(Collectors.toList())));
        });
    }
}
