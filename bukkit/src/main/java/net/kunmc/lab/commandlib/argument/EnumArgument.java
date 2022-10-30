package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.Arrays;
import java.util.function.Consumer;

public class EnumArgument<T extends Enum<T>> extends Argument<T> {
    private final Class<T> clazz;

    public EnumArgument(String name, Class<T> clazz) {
        this(name, clazz, option -> {
        });
    }

    public EnumArgument(String name, Class<T> clazz, Consumer<Option<T>> options) {
        super(name, StringArgumentType.string());

        this.clazz = clazz;
        setSuggestionAction(sb -> {
            Arrays.stream(clazz.getEnumConstants())
                    .filter(x -> filter() == null || filter().test(x))
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    public T cast(Object parsedArgument) {
        return clazz.cast(parsedArgument);
    }

    @Override
    public T parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return Arrays.stream(clazz.getEnumConstants())
                .filter(x -> x.name().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
