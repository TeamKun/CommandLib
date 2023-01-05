package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.Arrays;
import java.util.function.Consumer;

public abstract class AbstractEnumArgument<T extends Enum<T>, C extends AbstractCommandContext<?, ?>> extends CommonArgument<T, C> {
    private final Class<T> clazz;

    public AbstractEnumArgument(String name, Class<T> clazz) {
        this(name, clazz, option -> {
        });
    }

    public AbstractEnumArgument(String name, Class<T> clazz, Consumer<CommonArgument.Option<T, C>> options) {
        super(name, StringArgumentType.string());

        this.clazz = clazz;
        setSuggestionAction(sb -> {
            Arrays.stream(clazz.getEnumConstants())
                  .filter(x -> filter() == null || filter().test(x))
                  .map(Enum::name)
                  .map(String::toLowerCase)
                  .filter(x -> sb.getLatestInput()
                                 .isEmpty() || x.contains(sb.getLatestInput()))
                  .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    public T cast(Object parsedArgument) {
        return clazz.cast(parsedArgument);
    }

    @Override
    public T parse(C ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name);
        return Arrays.stream(clazz.getEnumConstants())
                     .filter(x -> x.name()
                                   .equalsIgnoreCase(s))
                     .findFirst()
                     .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
