package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.Nameable;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.function.Predicate;

public class ObjectArgument<T extends Nameable> extends Argument<T> {
    private final Collection<? extends T> candidates;
    private final Predicate<? super T> filter;

    public ObjectArgument(String name, Collection<? extends T> candidates, Predicate<? super T> filter, ContextAction contextAction) {
        super(name, sb -> {
            candidates.stream()
                    .filter(x -> filter == null || filter.test(x))
                    .map(Nameable::tabCompleteName)
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        checkPreconditions(candidates);
        this.candidates = candidates;
        this.filter = filter;
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
    public T parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return candidates.stream()
                .filter(x -> filter == null || filter.test(x))
                .filter(x -> x.tabCompleteName().equals(s))
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
