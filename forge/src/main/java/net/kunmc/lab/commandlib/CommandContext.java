package net.kunmc.lab.commandlib;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.kunmc.lab.commandlib.util.Location;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public final class CommandContext {
    private final com.mojang.brigadier.context.CommandContext<CommandSource> handle;
    private final Map<String, String> argumentNameToInputArgMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, Object> parsedArgMap = new LinkedHashMap<>();

    public CommandContext(com.mojang.brigadier.context.CommandContext<CommandSource> ctx) {
        this.handle = ctx;

        ctx.getNodes()
           .forEach(x -> {
               argumentNameToInputArgMap.put(x.getNode()
                                              .getName(),
                                             x.getRange()
                                              .get(ctx.getInput()));
           });
    }

    public com.mojang.brigadier.context.CommandContext<CommandSource> getHandle() {
        return handle;
    }

    public String getInput(String name) {
        return argumentNameToInputArgMap.getOrDefault(name, "");
    }

    public String getArg(int index) {
        return getArgs().get(index);
    }

    public List<String> getArgs() {
        return new ArrayList<>(argumentNameToInputArgMap.values());
    }

    public List<Object> getParsedArgs() {
        return Arrays.asList(parsedArgMap.values()
                                         .toArray());
    }

    public Object getParsedArg(int index) {
        return getParsedArgs().get(index);
    }

    public Object getParsedArg(String name) {
        return parsedArgMap.get(name);
    }

    public <T> T getParsedArg(int index, Class<T> clazz) {
        Object parsedArg = getParsedArg(index);
        if (parsedArg == null) {
            return null;
        }

        return clazz.cast(parsedArg);
    }

    public <T> T getParsedArg(String name, Class<T> clazz) {
        Object parsedArg = getParsedArg(name);
        if (parsedArg == null) {
            return null;
        }

        return clazz.cast(parsedArg);
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

    public void sendMessage(@Nullable Object obj) {
        sendMessage(Objects.toString(obj));
    }

    public void sendMessage(@Nullable String message) {
        sendMessage(message, false);
    }

    public void sendMessage(@Nullable String message, boolean allowLogging) {
        sendMessage(new StringTextComponent(String.valueOf(message)), allowLogging);

    }

    public void sendMessageWithOption(@Nullable Object obj, @NotNull Consumer<MessageOption> options) {
        sendMessageWithOption(Objects.toString(obj), options);
    }

    public void sendMessageWithOption(@Nullable String message, @NotNull Consumer<MessageOption> options) {
        MessageOption option = new MessageOption();
        options.accept(option);

        TextComponent component = new StringTextComponent(String.valueOf(message));
        component.setStyle(component.getStyle()
                                    .setColor(Color.fromInt(option.rgb))
                                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                                  new StringTextComponent(option.hoverText))));
        sendMessage(component);
    }


    public void sendMessage(@NotNull ITextComponent component) {
        sendMessage(component, false);
    }

    public void sendMessage(@NotNull ITextComponent component, boolean allowLogging) {
        getSender().sendFeedback(component, allowLogging);
    }

    public void sendSuccess(@Nullable Object obj) {
        sendSuccess(Objects.toString(obj));
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

    public void sendWarn(@Nullable Object obj) {
        sendWarn(Objects.toString(obj));
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

    public void sendFailure(@Nullable Object obj) {
        sendFailure(Objects.toString(obj));
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

    void addParsedArgument(String name, Object parsedArgument) {
        parsedArgMap.put(name, parsedArgument);
    }

    @Accessors(chain = true, fluent = true)
    @Setter
    public static class MessageOption {
        private int rgb = 0xFFFFFF;
        private String hoverText = "";

        private MessageOption() {
        }
    }
}
