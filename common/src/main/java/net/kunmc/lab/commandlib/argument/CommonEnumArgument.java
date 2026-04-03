package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class CommonEnumArgument<T extends Enum<T>, C extends AbstractCommandContext<?, ?>> extends CommonArgument<T, C> {
    private final Class<T> clazz;

    public CommonEnumArgument(String name, Class<T> clazz) {
        this(name, clazz, option -> {
        });
    }

    public CommonEnumArgument(String name, Class<T> clazz, Consumer<CommonArgument.Option<T, C>> options) {
        super(name, StringArgumentType.string());

        this.clazz = Objects.requireNonNull(clazz);
        suggestionAction(sb -> {
            Arrays.stream(clazz.getEnumConstants())
                  .filter(filter(sb.getContext()))
                  .map(Enum::name)
                  .map(String::toLowerCase)
                  .filter(x -> sb.getLatestInput()
                                 .isEmpty() || x.contains(sb.getLatestInput()))
                  .forEach(sb::suggest);
        });
        applyOptions(options);
    }

    @Override
    public final T cast(Object parsedArgument) {
        return clazz.cast(parsedArgument);
    }

    @Override
    protected final T parseImpl(C ctx) throws ArgumentParseException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());
        return Arrays.stream(clazz.getEnumConstants())
                     .filter(x -> x.name()
                                   .equalsIgnoreCase(s))
                     .findFirst()
                     .orElseThrow(() -> ArgumentParseException.ofIncorrectInput(this.name(), ctx, s));
    }
}
