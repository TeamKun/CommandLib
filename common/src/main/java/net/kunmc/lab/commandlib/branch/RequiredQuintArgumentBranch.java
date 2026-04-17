package net.kunmc.lab.commandlib.branch;

import net.kunmc.lab.commandlib.*;
import net.kunmc.lab.commandlib.util.function.HeptConsumer;
import net.kunmc.lab.commandlib.util.function.QuintFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;

public final class RequiredQuintArgumentBranch<S, T1, T2, T3, T4, T5, C extends AbstractCommandContext<?, ?>, T extends CommonCommand<C, ?, T>> extends RequiredArgumentBranch<S, C, T> {
    private final CommonArgument<T1, C> argument1;
    private final CommonArgument<T2, C> argument2;
    private final CommonArgument<T3, C> argument3;
    private final CommonArgument<T4, C> argument4;
    private final CommonArgument<T5, C> argument5;

    public RequiredQuintArgumentBranch(@NotNull Extractor<C, S> extractor,
                                       @NotNull Consumer<CommandHandler<C>> executeRegistrar,
                                       @NotNull Consumer<Collection<? extends T>> childrenAdder,
                                       @NotNull CommonArgument<T1, C> argument1,
                                       @NotNull CommonArgument<T2, C> argument2,
                                       @NotNull CommonArgument<T3, C> argument3,
                                       @NotNull CommonArgument<T4, C> argument4,
                                       @NotNull CommonArgument<T5, C> argument5) {
        super(extractor, executeRegistrar, childrenAdder);
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.argument3 = argument3;
        this.argument4 = argument4;
        this.argument5 = argument5;
    }

    public RequiredQuintArgumentBranch<S, T1, T2, T3, T4, T5, C, T> execute(@NotNull HeptConsumer<T1, T2, T3, T4, T5, S, C> action) {
        super.execute((s, ctx) -> action.accept(ctx.getParsedArg(argument1),
                                                ctx.getParsedArg(argument2),
                                                ctx.getParsedArg(argument3),
                                                ctx.getParsedArg(argument4),
                                                ctx.getParsedArg(argument5),
                                                s,
                                                ctx));
        return this;
    }

    public RequiredQuintArgumentBranch<S, T1, T2, T3, T4, T5, C, T> child(@NotNull QuintFunction<CommonArgument<T1, C>, CommonArgument<T2, C>, CommonArgument<T3, C>, CommonArgument<T4, C>, CommonArgument<T5, C>, T> factory) {
        super.child(factory.apply(argument1, argument2, argument3, argument4, argument5));
        return this;
    }
}
