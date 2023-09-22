package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommonObjectArgument<T, C extends AbstractCommandContext<?, ?>> extends CommonArgument<T, C> {
    private final Supplier<Map<String, ? extends T>> mapSupplier;

    public CommonObjectArgument(String name, Map<String, ? extends T> nameToObjectMap) {
        this(name, nameToObjectMap, option -> {
        });
    }

    public CommonObjectArgument(String name, Map<String, ? extends T> nameToObjectMap, Consumer<Option<T, C>> options) {
        this(name, () -> nameToObjectMap, options);
    }

    public CommonObjectArgument(String name, Supplier<Map<String, ? extends T>> mapSupplier) {
        this(name, mapSupplier, option -> {
        });
    }

    public CommonObjectArgument(String name,
                                Supplier<Map<String, ? extends T>> mapSupplier,
                                Consumer<Option<T, C>> options) {
        super(name, StringArgumentType.string());
        this.mapSupplier = Objects.requireNonNull(mapSupplier);

        setSuggestionAction(sb -> {
            mapSupplier.get()
                       .entrySet()
                       .stream()
                       .filter(x -> filter().test(x.getValue()))
                       .map(Map.Entry::getKey)
                       .filter(x -> sb.getLatestInput()
                                      .isEmpty() || x.contains(sb.getLatestInput()))
                       .forEach(sb::suggest);
        });
        applyOptions(options);
    }

    @Override
    public final T cast(Object parsedArgument) {
        return ((T) parsedArgument);
    }

    @Override
    protected final T parseImpl(C ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());
        return mapSupplier.get()
                          .entrySet()
                          .stream()
                          .filter(x -> x.getKey()
                                        .equals(s))
                          .map(Map.Entry::getValue)
                          .findFirst()
                          .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
