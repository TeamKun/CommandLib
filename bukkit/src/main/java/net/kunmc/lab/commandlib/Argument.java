package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Argument<T> {
    protected final String name;
    private SuggestionAction suggestionAction;
    private ContextAction contextAction;
    private final ArgumentType<?> type;

    public Argument(String name, ArgumentType<?> type) {
        this.name = name;
        this.type = type;
    }

    public Argument(String name, SuggestionAction suggestionAction, ContextAction contextAction, ArgumentType<?> type) {
        this.name = name;
        this.suggestionAction = suggestionAction;
        this.contextAction = contextAction;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public SuggestionAction suggestionAction() {
        return suggestionAction;
    }

    protected void setSuggestionAction(SuggestionAction suggestionAction) {
        this.suggestionAction = suggestionAction;
    }

    public ContextAction contextAction() {
        return contextAction;
    }

    public ArgumentType<?> type() {
        return type;
    }

    protected void setContextAction(ContextAction contextAction) {
        this.contextAction = contextAction;
    }

    boolean hasContextAction() {
        return contextAction != null;
    }

    String generateHelpMessageTag() {
        return String.format(ChatColor.GRAY + "<" + ChatColor.YELLOW + "%s" + ChatColor.GRAY + ">", name);
    }

    protected static IncorrectArgumentInputException convertSyntaxException(CommandSyntaxException e) {
        ChatMessage msg = ((ChatMessage) e.getRawMessage());
        TranslatableComponent component = Component.translatable()
                .key((msg.getKey()))
                .args(Arrays.stream(msg.getArgs())
                        .map(Object::toString)
                        .map(Component::text)
                        .collect(Collectors.toList()))
                .color(TextColor.color(ChatColor.RED.asBungee().getColor().getRGB()))
                .build();

        return new IncorrectArgumentInputException(component);
    }

    protected static String getInputString(CommandContext<CommandListenerWrapper> ctx, String name) {
        try {
            Field f = ctx.getClass().getDeclaredField("arguments");
            f.setAccessible(true);
            ParsedArgument<CommandListenerWrapper, ?> argument = ((Map<String, ParsedArgument<CommandListenerWrapper, ?>>) f.get(ctx)).get(name);
            return argument.getRange().get(ctx.getInput());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract T parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException;
}
