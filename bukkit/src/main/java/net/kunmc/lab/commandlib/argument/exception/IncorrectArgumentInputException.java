package net.kunmc.lab.commandlib.argument.exception;

import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import net.kunmc.lab.commandlib.Argument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class IncorrectArgumentInputException extends Exception {
    private final List<Component> components;

    public IncorrectArgumentInputException(Argument<?> argument, CommandContext<CommandListenerWrapper> ctx, String incorrectInput) {
        String input = ctx.getInput();
        Component unknownArgumentMsg = Component.translatable("command.unknown.argument", TextColor.color(ChatColor.RED.asBungee().getColor().getRGB()), Component.text(incorrectInput));

        StringRange range = ctx.getNodes().stream()
                .filter(n -> n.getNode().getName().equals(argument.name()))
                .findFirst()
                .get()
                .getRange();
        String s = input.substring(1, range.getStart());
        if (s.length() > 10) {
            s = "..." + s.substring(s.length() - 10);
        }
        Component hereMsg = Component.text(ChatColor.GRAY + s + ChatColor.RED + ChatColor.UNDERLINE + incorrectInput + ChatColor.RESET)
                .append(Component.translatable("command.context.here", Style.style().decorate(TextDecoration.ITALIC).color(TextColor.color(ChatColor.RED.asBungee().getColor().getRGB())).build()));

        this.components = Lists.newArrayList(unknownArgumentMsg, hereMsg);
    }

    public IncorrectArgumentInputException(Component component, Component... components) {
        this.components = Lists.asList(component, components);
    }

    public void sendMessage(CommandSender sender) {
        components.forEach(sender::sendMessage);
    }
}