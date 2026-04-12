package net.kunmc.lab.commandlib;

import com.mojang.brigadier.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;

public final class CommandOption<T, C extends AbstractCommandContext<?, ?>> {
    static final String INTERNAL_NAME_PREFIX = "__commandlib_option_";

    private final String name;
    private final Character shortName;
    private final T defaultValue;
    private final ArgumentType<?> type;
    private final BiFunction<C, String, T> parser;
    private final String internalName;
    private String description = "";

    public CommandOption(@NotNull String name, @Nullable Character shortName, @NotNull T defaultValue) {
        this(name, shortName, defaultValue, null, null);
    }

    public CommandOption(@NotNull String name,
                         @Nullable Character shortName,
                         @NotNull T defaultValue,
                         @Nullable ArgumentType<?> type,
                         @Nullable BiFunction<C, String, T> parser) {
        this.name = Objects.requireNonNull(name);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is not expected to empty.");
        }
        this.shortName = shortName;
        this.defaultValue = Objects.requireNonNull(defaultValue);
        this.type = type;
        this.parser = parser;
        this.internalName = INTERNAL_NAME_PREFIX + name;
    }

    public String name() {
        return name;
    }

    public CommandOption<T, C> description(@NotNull String description) {
        this.description = Objects.requireNonNull(description);
        return this;
    }

    public String description() {
        return description;
    }

    @Nullable
    public Character shortName() {
        return shortName;
    }

    T defaultValue() {
        return defaultValue;
    }

    boolean hasValue() {
        return type != null;
    }

    @Nullable ArgumentType<?> type() {
        return type;
    }

    String internalName() {
        return internalName;
    }

    T parse(C ctx) {
        return Objects.requireNonNull(parser)
                      .apply(ctx, internalName);
    }

    @SuppressWarnings("unchecked")
    T cast(Object value) {
        return (T) value;
    }
}
