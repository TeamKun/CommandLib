package net.kunmc.lab.commandlib.branch;

import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.command.Extractor;
import net.kunmc.lab.commandlib.command.CommandHandler;
import net.kunmc.lab.commandlib.*;
import net.kunmc.lab.commandlib.util.function.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public final class RequiredUnaryArgumentBranch<S, T1, C extends AbstractCommandContext<?, ?>, T extends CommonCommand<C, ?, T>> extends RequiredArgumentBranch<S, C, T> {
    private final CommonArgument<T1, C> argument1;

    public RequiredUnaryArgumentBranch(@NotNull Extractor<C, S> extractor,
                                       @NotNull Consumer<CommandHandler<C>> executeRegistrar,
                                       @NotNull Consumer<Collection<? extends T>> childrenAdder,
                                       @NotNull CommonArgument<T1, C> argument1) {
        super(extractor, executeRegistrar, childrenAdder);
        this.argument1 = argument1;
    }

    public RequiredUnaryArgumentBranch<S, T1, C, T> execute(@NotNull TriConsumer<T1, S, C> action) {
        super.execute((s, ctx) -> action.accept(ctx.getArgument(argument1), s, ctx));
        return this;
    }

    public RequiredUnaryArgumentBranch<S, T1, C, T> child(@NotNull Function<CommonArgument<T1, C>, T> factory) {
        super.child(factory.apply(argument1));
        return this;
    }
}
