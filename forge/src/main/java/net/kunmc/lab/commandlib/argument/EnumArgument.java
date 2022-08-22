package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;

import java.util.Arrays;
import java.util.function.Predicate;

public class EnumArgument<T extends Enum<T>> extends Argument<T> {
    private final Class<T> clazz;
    private final Predicate<? super T> filter;

    public EnumArgument(String name, Class<T> clazz, Predicate<? super T> filter, ContextAction contextAction) {
        super(name, sb -> {
            Arrays.stream(clazz.getEnumConstants())
                    .filter(x -> filter == null || filter.test(x))
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.clazz = clazz;
        this.filter = filter;
    }

    @Override
    public T parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return Arrays.stream(clazz.getEnumConstants())
                .filter(x -> filter == null || filter.test(x))
                .filter(x -> x.name().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
