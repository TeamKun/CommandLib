package net.kunmc.lab.commandlib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class AbstractCommandContext<S, C> {
    protected final com.mojang.brigadier.context.CommandContext<S> handle;
    private final Map<String, String> argumentNameToInputArgMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, Object> parsedArgMap = new LinkedHashMap<>();
    private final PlatformAdapter<S, C, ?, ?, ?> platformAdapter;

    protected AbstractCommandContext(com.mojang.brigadier.context.CommandContext<S> ctx,
                                     PlatformAdapter<S, C, ?, ?, ?> platformAdapter) {
        this.handle = ctx;
        this.platformAdapter = platformAdapter;

        ctx.getNodes()
           .forEach(x -> {
               argumentNameToInputArgMap.put(x.getNode()
                                              .getName(),
                                             x.getRange()
                                              .get(ctx.getInput()));
           });
    }

    public final com.mojang.brigadier.context.CommandContext<S> getHandle() {
        return handle;
    }

    public final String getInput(String name) {
        return argumentNameToInputArgMap.getOrDefault(name, "");
    }

    public final String getArg(int index) {
        return getArgs().get(index);
    }

    public final List<String> getArgs() {
        return new ArrayList<>(argumentNameToInputArgMap.values());
    }

    public final List<Object> getParsedArgs() {
        return Arrays.asList(parsedArgMap.values()
                                         .toArray());
    }

    public final Object getParsedArg(int index) {
        return getParsedArgs().get(index);
    }

    public final Object getParsedArg(String name) {
        return parsedArgMap.get(name);
    }

    public final <T> T getParsedArg(int index, Class<T> clazz) {
        Object parsedArg = getParsedArg(index);
        if (parsedArg == null) {
            return null;
        }

        return clazz.cast(parsedArg);
    }

    public final <T> T getParsedArg(String name, Class<T> clazz) {
        Object parsedArg = getParsedArg(name);
        if (parsedArg == null) {
            return null;
        }

        return clazz.cast(parsedArg);
    }

    public final void sendMessage(@Nullable Object obj) {
        sendMessage(Objects.toString(obj));
    }

    public abstract void sendMessage(@Nullable String message);

    public final void sendMessageWithOption(@Nullable Object obj, @NotNull Consumer<MessageOption> options) {
        sendMessageWithOption(Objects.toString(obj), options);
    }

    public abstract void sendMessageWithOption(@Nullable String message, @NotNull Consumer<MessageOption> options);

    public final void sendSuccess(@Nullable Object obj) {
        sendSuccess(Objects.toString(obj));
    }

    public abstract void sendSuccess(@Nullable String message);

    public final void sendWarn(@Nullable Object obj) {
        sendWarn(Objects.toString(obj));
    }

    public abstract void sendWarn(@Nullable String message);

    public final void sendFailure(@Nullable Object obj) {
        sendFailure(Objects.toString(obj));
    }

    public abstract void sendFailure(@Nullable String message);

    public abstract void sendComponent(C component);

    public final PlatformAdapter<S, C, ?, ?, ?> platformAdapter() {
        return platformAdapter;
    }

    final void addParsedArgument(String name, Object parsedArgument) {
        parsedArgMap.put(name, parsedArgument);
    }

    public static class MessageOption {
        private int rgb = 0xFFFFFF;
        private String hoverText = "";

        protected static <T> T createMessage(Consumer<MessageOption> optionConsumer,
                                             BiFunction<Integer, String, T> messageCreator) {
            MessageOption option = new MessageOption();
            optionConsumer.accept(option);
            return messageCreator.apply(option.rgb, option.hoverText);
        }

        private MessageOption() {
        }

        public MessageOption rgb(int rgb) {
            this.rgb = rgb;
            return this;
        }

        public MessageOption hoverText(String hoverText) {
            this.hoverText = hoverText;
            return this;
        }
    }
}
