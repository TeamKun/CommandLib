package net.kunmc.lab.commandlib.exception;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class IncorrectArgumentInputException extends Exception {
    private final List<ITextComponent> components;

    public IncorrectArgumentInputException(ITextComponent component, ITextComponent... components) {
        this.components = Lists.asList(component, components);
    }

    public void sendMessage(CommandSource sender) {
        components.forEach(sender::sendErrorMessage);
    }
}
