package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.Nameable;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.function.Consumer;

public class ObjectArgument<T extends Nameable> extends Argument<T> {
    private final Collection<? extends T> candidates;

    public ObjectArgument(String name, Collection<? extends T> candidates) {
        this(name, candidates, option -> {
        });
    }

    public ObjectArgument(String name, Collection<? extends T> candidates, Consumer<Option<T>> options) {
        super(name, StringArgumentType.string());
        checkPreconditions(candidates);
        this.candidates = candidates;

        setSuggestionAction(sb -> {
            candidates.stream()
                    .filter(x -> filter() == null || filter().test(x))
                    .map(Nameable::tabCompleteName)
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        });
        setOptions(options);
    }

    private static void checkPreconditions(Collection<? extends Nameable> candidates) {
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("candidates is empty.");
        }

        if (candidates.stream()
                .map(Nameable::tabCompleteName)
                .distinct()
                .count() != candidates.size()) {
            throw new IllegalArgumentException("candidates has duplicate name elements.");
        }
    }

    @Override
    protected T cast(Object parsedArgument) {
        return ((T) parsedArgument);
    }

    @Override
    public T parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return candidates.stream()
                .filter(x -> x.tabCompleteName().equals(s))
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
