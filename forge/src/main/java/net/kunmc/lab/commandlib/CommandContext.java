package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.Location;
import net.kunmc.lab.commandlib.util.text.TextComponentBuilder;
import net.kunmc.lab.commandlib.util.text.TextComponentBuilderImpl;
import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilder;
import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilderImpl;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;

public final class CommandContext extends AbstractCommandContext<CommandSource, TextComponent> {
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

    public void sendMessage(@Nullable String message) {
        sendMessage(message, false);
    }

    public void sendMessage(@Nullable String message, boolean allowLogging) {
        sendMessage(new StringTextComponent(String.valueOf(message)), allowLogging);

    }

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

    public void sendTranslatableWithOption(@NotNull String key,
                                           @NotNull Object[] args,
                                           @NotNull Consumer<MessageOption> options) {
        ITextComponent messageComponent = MessageOption.createMessage(options, (rgb, hoverText) -> {
            TextComponent c = new TranslationTextComponent(key, args);
            return c.setStyle(c.getStyle()
                               .setColor(Color.fromInt(rgb))
                               .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                             new StringTextComponent(hoverText))));
        });

        sendMessage(messageComponent);
    }


    public void sendMessage(@NotNull ITextComponent component) {
        sendMessage(component, false);
    }

    public void sendMessage(@NotNull ITextComponent component, boolean allowLogging) {
        getSender().sendFeedback(component, allowLogging);
    }

    public void sendSuccess(@Nullable String message) {
        sendSuccess(message, false);
    }

    public void sendSuccess(@Nullable String message, boolean allowLogging) {
        TextComponent component = new StringTextComponent(String.valueOf(message));
        component.setStyle(component.getStyle()
                                    .setColor(Color.fromInt(TextFormatting.GREEN.getColor())));
        sendMessage(component, allowLogging);
    }

    public void sendWarn(@Nullable String message) {
        sendWarn(message, false);
    }

    public void sendWarn(@Nullable String message, boolean allowLogging) {
        TextComponent component = new StringTextComponent(String.valueOf(message));
        component.setStyle(component.getStyle()
                                    .setColor(Color.fromInt(TextFormatting.YELLOW.getColor())));
        sendMessage(component, allowLogging);
    }

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
    public void sendComponentBuilders(ComponentBuilder<?, ?> builder, ComponentBuilder<?, ?>... builders) {
        TextComponent c = (TextComponent) (builder.build());
        Arrays.stream(builders)
              .map(ComponentBuilder::build)
              .map(TextComponent.class::cast)
              .forEach(c::appendSibling);
        sendMessage(c);
    }

    @Override
    public TextComponentBuilder<? extends TextComponent, ?> textComponentBuilder(@NotNull String text) {
        return new TextComponentBuilderImpl(text);
    }

    @Override
    public TranslatableComponentBuilder<? extends TextComponent, ?> translatableComponentBuilder(@NotNull String key) {
        return new TranslatableComponentBuilderImpl(key);
    }

    @Override
    IncorrectArgumentInputException convertCommandSyntaxException(CommandSyntaxException e) {
        return new IncorrectArgumentInputException(ctx -> {
            ((CommandContext) ctx).sendMessage(((ITextComponent) e.getRawMessage()));
        });
    }
}
