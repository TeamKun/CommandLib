package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractObjectArgument<T, C extends AbstractCommandContext<?, ?>> extends CommonArgument<T, C> {
    private final Map<String, ? extends T> nameToObjectMap;

    public AbstractObjectArgument(String name, Map<String, ? extends T> nameToObjectMap) {
        this(name, nameToObjectMap, option -> {
        });
    }

    public AbstractObjectArgument(String name,
                                  Map<String, ? extends T> nameToObjectMap,
                                  Consumer<CommonArgument.Option<T, C>> options) {
        super(name, StringArgumentType.string());
        this.nameToObjectMap = nameToObjectMap;

        setSuggestionAction(sb -> {
            nameToObjectMap.entrySet()
                           .stream()
                           .filter(x -> filter() == null || filter().test(x.getValue()))
                           .map(Map.Entry::getKey)
                           .filter(x -> sb.getLatestInput()
                                          .isEmpty() || x.contains(sb.getLatestInput()))
                           .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    public T cast(Object parsedArgument) {
        return ((T) parsedArgument);
    }

    @Override
    public T parse(C ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name);
        return nameToObjectMap.entrySet()
                              .stream()
                              .filter(x -> x.getKey()
                                            .equals(s))
                              .map(Map.Entry::getValue)
                              .findFirst()
                              .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
