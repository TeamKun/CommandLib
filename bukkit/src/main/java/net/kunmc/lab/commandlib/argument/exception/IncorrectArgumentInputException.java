package net.kunmc.lab.commandlib.argument.exception;

import com.google.common.collect.Lists;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.v1_16_R3.ChatMessage;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class IncorrectArgumentInputException extends Exception {
    private final List<Component> components;

    public static IncorrectArgumentInputException fromCommandSyntaxException(CommandSyntaxException e) {
        ChatMessage msg = ((ChatMessage) e.getRawMessage());
        TranslatableComponent component = Component.translatable()
                                                   .key((msg.getKey()))
                                                   .args(Arrays.stream(msg.getArgs())
                                                               .map(Object::toString)
                                                               .map(Component::text)
                                                               .collect(Collectors.toList()))
                                                   .color(TextColor.color(ChatColor.RED.asBungee()
                                                                                       .getColor()
                                                                                       .getRGB()))
                                                   .build();

        return new IncorrectArgumentInputException(component);
    }

    public IncorrectArgumentInputException(Argument<?> argument, CommandContext ctx, String incorrectInput) {
        String input = ctx.getHandle()
                          .getInput();
        Component unknownArgumentMsg = Component.translatable("command.unknown.argument",
                                                              TextColor.color(ChatColor.RED.asBungee()
                                                                                           .getColor()
                                                                                           .getRGB()),
                                                              Component.text(incorrectInput));

        StringRange range = ctx.getHandle()
                               .getNodes()
                               .stream()
                               .filter(n -> n.getNode()
                                             .getName()
                                             .equals(argument.name()))
                               .findFirst()
                               .get()
                               .getRange();
        String s = input.substring(1, range.getStart());
        if (s.length() > 10) {
            s = "..." + s.substring(s.length() - 10);
        }
        Component hereMsg = Component.text(ChatColor.GRAY + s + ChatColor.RED + ChatColor.UNDERLINE + incorrectInput + ChatColor.RESET)
                                     .append(Component.translatable("command.context.here",
                                                                    Style.style()
                                                                         .decorate(TextDecoration.ITALIC)
                                                                         .color(TextColor.color(ChatColor.RED.asBungee()
                                                                                                             .getColor()
                                                                                                             .getRGB()))
                                                                         .build()));

        this.components = Lists.newArrayList(unknownArgumentMsg, hereMsg);
    }

    public IncorrectArgumentInputException(Component component, Component... components) {
        this.components = Lists.asList(component, components);
    }

    public void sendMessage(CommandContext context) {
        components.forEach(context::sendMessage);
    }
}
