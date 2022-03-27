package net.kunmc.lab.commandlib.exception;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.List;

public class IncorrectArgumentInputException extends Exception {
    private final List<Component> components;

    public IncorrectArgumentInputException(Component component, Component... components) {
        this.components = Lists.asList(component, components);
    }

    public void sendMessage(CommandSender sender) {
        components.forEach(sender::sendMessage);
    }
}