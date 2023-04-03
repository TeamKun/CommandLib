package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.Nameable;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.Collection;
import java.util.function.Consumer;

public class CommonNameableObjectArgument<T extends Nameable, C extends AbstractCommandContext<?, ?>> extends CommonArgument<T, C> {
    private final Collection<? extends T> candidates;

    public CommonNameableObjectArgument(String name, Collection<? extends T> candidates) {
        this(name, candidates, option -> {
        });
    }

    public CommonNameableObjectArgument(String name,
                                        Collection<? extends T> candidates,
                                        Consumer<Option<T, C>> options) {
        super(name, StringArgumentType.string());
        checkPreconditions(candidates);
        this.candidates = candidates;

        setSuggestionAction(sb -> {
            candidates.stream()
                      .filter(x -> test(x, true))
                      .map(Nameable::tabCompleteName)
                      .filter(x -> sb.getLatestInput()
                                     .isEmpty() || x.contains(sb.getLatestInput()))
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
    public final T cast(Object parsedArgument) {
        return ((T) parsedArgument);
    }

    @Override
    public final T parse(C ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name);
        return candidates.stream()
                         .filter(x -> x.tabCompleteName()
                                       .equals(s))
                         .findFirst()
                         .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
