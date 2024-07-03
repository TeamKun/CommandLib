package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.util.Location;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public final class CommandContext extends AbstractCommandContext<CommandSource, ITextComponent> {
    public CommandContext(com.mojang.brigadier.context.CommandContext<CommandSource> ctx) {
        super(ctx);
    }

    public Entity getEntity() {
        return handle.getSource()
                     .getEntity();
    }

    public ServerWorld getWorld() {
        return handle.getSource()
                     .getWorld();
    }

    public Location getLocation() {
        return new Location(getWorld(),
                            handle.getSource()
                                  .getPos());
    }

    public CommandSource getSender() {
        return handle.getSource();
    }

    @Override
    public void sendMessage(@Nullable String message) {
        sendMessage(message, false);
    }

    public void sendMessage(@Nullable String message, boolean allowLogging) {
        sendMessage(new StringTextComponent(String.valueOf(message)), allowLogging);

    }

    public void sendMessage(@NotNull ITextComponent component) {
        sendMessage(component, false);
    }

    public void sendMessage(@NotNull ITextComponent component, boolean allowLogging) {
        getSender().sendFeedback(Objects.requireNonNull(component), allowLogging);
    }

    @Override
    public void sendSuccess(@Nullable String message) {
        sendSuccess(message, false);
    }

    public void sendSuccess(@Nullable String message, boolean allowLogging) {
        TextComponent component = new StringTextComponent(String.valueOf(message));
        component.setStyle(component.getStyle()
                                    .setColor(Color.fromInt(TextFormatting.GREEN.getColor())));
        sendMessage(component, allowLogging);
    }

    @Override
    public void sendWarn(@Nullable String message) {
        sendWarn(message, false);
    }

    public void sendWarn(@Nullable String message, boolean allowLogging) {
        TextComponent component = new StringTextComponent(String.valueOf(message));
        component.setStyle(component.getStyle()
                                    .setColor(Color.fromInt(TextFormatting.YELLOW.getColor())));
        sendMessage(component, allowLogging);
    }

    @Override
    public void sendFailure(@Nullable String message) {
        sendFailure(message, false);
    }

    public void sendFailure(@Nullable String message, boolean allowLogging) {
        TextComponent component = new StringTextComponent(String.valueOf(message));
        component.setStyle(component.getStyle()
                                    .setColor(Color.fromInt(TextFormatting.RED.getColor())));
        sendMessage(component, allowLogging);
    }

    @Override
    public void sendMessageWithOption(@Nullable String message, @NotNull Consumer<MessageOption> options) {
        ITextComponent messageComponent = MessageOption.createMessage(options, (rgb, hoverText) -> {
            TextComponent c = new StringTextComponent(String.valueOf(message));
            return c.setStyle(c.getStyle()
                               .setColor(Color.fromInt(rgb))
                               .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                             new StringTextComponent(hoverText))));
        });

        sendMessage(messageComponent);
    }

    @Override
    public void sendComponent(ITextComponent component) {
        sendMessage(component);
    }
}
