package net.kunmc.lab.commandlib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractCommandContext<S, C> {
    protected final com.mojang.brigadier.context.CommandContext<S> handle;
    private final Map<String, String> argumentNameToInputArgMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, Object> parsedArgMap = new LinkedHashMap<>();

    protected AbstractCommandContext(@NotNull com.mojang.brigadier.context.CommandContext<S> ctx) {
        this.handle = Objects.requireNonNull(ctx);

        ctx.getNodes()
           .forEach(x -> {
               argumentNameToInputArgMap.put(x.getNode()
                                              .getName(),
                                             x.getRange()
                                              .get(ctx.getInput()));
           });
    }

    @NotNull
    public final com.mojang.brigadier.context.CommandContext<S> getHandle() {
        return handle;
    }

    @NotNull
    public final String getInput(String name) {
        return argumentNameToInputArgMap.getOrDefault(name, "");
    }

    /**
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())n
     */
    @NotNull
    public final String getArg(int index) {
        return getArgs().get(index);
    }

    @NotNull
    public final List<String> getArgs() {
        return new ArrayList<>(argumentNameToInputArgMap.values());
    }

    @NotNull
    public final List<Object> getParsedArgs() {
        return Arrays.asList(parsedArgMap.values()
                                         .toArray());
    }

    /**
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())n
     */
    @NotNull
    public final Object getParsedArg(int index) {
        return getParsedArgs().get(index);
    }

    @Nullable
    public final Object getParsedArg(String name) {
        return parsedArgMap.get(name);
    }

    /**
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())n
     */
    @NotNull
    public final <T> T getParsedArg(int index, Class<T> clazz) {
        Object parsedArg = getParsedArg(index);
        return clazz.cast(parsedArg);
    }

    @Nullable
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

    final void addParsedArgument(String name, Object parsedArgument) {
        parsedArgMap.put(name, parsedArgument);
    }
}
