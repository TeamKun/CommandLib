package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.Nameable;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommonNameableObjectArgument<T extends Nameable, C extends AbstractCommandContext<?, ?>> extends CommonArgument<T, C> {
    private final Supplier<Collection<? extends T>> candidatesSupplier;

    public CommonNameableObjectArgument(String name, Collection<? extends T> candidates) {
        this(name, candidates, option -> {
        });
    }

    public CommonNameableObjectArgument(String name,
                                        Collection<? extends T> candidates,
                                        Consumer<Option<T, C>> options) {
        this(name, () -> candidates, options);
    }

    public CommonNameableObjectArgument(String name, Supplier<Collection<? extends T>> candidatesSupplier) {
        this(name, candidatesSupplier, option -> {
        });
    }

    public CommonNameableObjectArgument(String name,
                                        Supplier<Collection<? extends T>> candidatesSupplier,
                                        Consumer<Option<T, C>> options) {
        super(name, StringArgumentType.string());
        this.candidatesSupplier = candidatesSupplier;

        setSuggestionAction(sb -> {
            candidatesSupplier.get()
                              .stream()
                              .filter(x -> test(x, true))
                              .map(Nameable::tabCompleteName)
                              .filter(x -> sb.getLatestInput()
                                             .isEmpty() || x.contains(sb.getLatestInput()))
                              .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    public final T cast(Object parsedArgument) {
        return ((T) parsedArgument);
    }

    @Override
    protected final T parseImpl(C ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());
        return candidatesSupplier.get()
                                 .stream()
                                 .filter(x -> x.tabCompleteName()
                                               .equals(s))
                                 .findFirst()
                                 .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
