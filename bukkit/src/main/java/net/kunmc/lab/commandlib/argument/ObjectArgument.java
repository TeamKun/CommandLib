package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.Map;
import java.util.function.Consumer;

public class ObjectArgument<T> extends Argument<T> {
    private final Map<String, ? extends T> nameToObjectMap;

    public ObjectArgument(String name, Map<String, ? extends T> nameToObjectMap) {
        this(name, nameToObjectMap, option -> {
        });
    }

    public ObjectArgument(String name, Map<String, ? extends T> nameToObjectMap, Consumer<Option<T>> options) {
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
    public T parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return nameToObjectMap.entrySet()
                              .stream()
                              .filter(x -> x.getKey()
                                            .equals(s))
                              .map(Map.Entry::getValue)
                              .findFirst()
                              .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
