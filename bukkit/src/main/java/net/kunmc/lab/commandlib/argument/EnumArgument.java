package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.Arrays;
import java.util.function.Predicate;

public class EnumArgument<T extends Enum<T>> extends Argument<T> {
    private final Class<T> clazz;
    private final Predicate<T> filter;

    public EnumArgument(String name, Class<T> clazz, Predicate<T> filter, ContextAction contextAction) {
        super(name, sb -> {
            Arrays.stream(clazz.getEnumConstants())
                    .filter(t -> {
                        if (filter != null) {
                            return filter.test(t);
                        }

                        return true;
                    })
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.clazz = clazz;
        this.filter = filter;
    }

    @Override
    public T parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return Arrays.stream(clazz.getEnumConstants())
                .filter(t -> {
                    if (filter != null) {
                        return filter.test(t);
                    }

                    return true;
                })
                .filter(x -> x.name().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(ctx.getInput(), s));
    }
}
