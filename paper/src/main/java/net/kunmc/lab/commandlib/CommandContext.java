package net.kunmc.lab.commandlib;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public final class CommandContext extends AbstractCommandContext<CommandSourceStack, Component> {
    public CommandContext(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        super(ctx);
    }

    @NotNull
    public CommandSender getSender() {
        return handle.getSource().getSender();
    }

    @NotNull
    public Player getPlayer() {
        CommandSender sender = getSender();
        if (!(sender instanceof Player)) {
            throw new IllegalStateException("sender is not a player");
        }
        return (Player) sender;
    }

    @NotNull
    public ConsoleCommandSender getConsole() {
        CommandSender sender = getSender();
        if (!(sender instanceof ConsoleCommandSender)) {
            throw new IllegalStateException("sender is not a console");
        }
        return (ConsoleCommandSender) sender;
    }

    @Override
    public String getLanguage() {
        CommandSender sender = getSender();
        if (sender instanceof Player) {
            String locale = ((Player) sender).getLocale();
            if (locale != null && !locale.isEmpty()) {
                return locale.toLowerCase(Locale.ROOT);
            }
        }
        return Locale.getDefault().toLanguageTag().toLowerCase(Locale.ROOT).replace('-', '_');
    }

    @Override
    public void sendMessage(@Nullable String message) {
        getSender().sendMessage(Component.text(Objects.toString(message, "")));
    }

    @Override
    public void sendSuccess(@Nullable String message) {
        getSender().sendMessage(Component.text(Objects.toString(message, "")).color(NamedTextColor.GREEN));
    }

    @Override
    public void sendWarn(@Nullable String message) {
        getSender().sendMessage(Component.text(Objects.toString(message, "")).color(NamedTextColor.YELLOW));
    }

    @Override
    public void sendFailure(@Nullable String message) {
        getSender().sendMessage(Component.text(Objects.toString(message, "")).color(NamedTextColor.RED));
    }

    @Override
    public void sendMessageWithOption(@Nullable String message, @NotNull Consumer<MessageOption> options) {
        Component component = MessageOption.createMessage(options, (rgb, hoverText) ->
                Component.text(Objects.toString(message, ""))
                         .color(TextColor.color(rgb))
                         .hoverEvent(HoverEvent.showText(Component.text(hoverText))));
        getSender().sendMessage(component);
    }

    @Override
    public void sendComponent(@NotNull Component component) {
        getSender().sendMessage(component);
    }
}
