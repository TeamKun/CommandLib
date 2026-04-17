package net.kunmc.lab.commandlib.branch;

import net.kunmc.lab.commandlib.*;
import net.kunmc.lab.commandlib.util.function.HeptFunction;
import net.kunmc.lab.commandlib.util.function.OctoConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HeptArgumentBranch<T1, T2, T3, T4, T5, T6, T7, C extends AbstractCommandContext<?, ?>, T extends CommonCommand<C, ?, T>> extends ArgumentBranch<C, T> {
    private final CommonArgument<T1, C> argument1;
    private final CommonArgument<T2, C> argument2;
    private final CommonArgument<T3, C> argument3;
    private final CommonArgument<T4, C> argument4;
    private final CommonArgument<T5, C> argument5;
    private final CommonArgument<T6, C> argument6;
    private final CommonArgument<T7, C> argument7;

    public HeptArgumentBranch(ArgumentBranchDelegate<C, T> delegate,
                              CommonArgument<T1, C> argument1,
                              CommonArgument<T2, C> argument2,
                              CommonArgument<T3, C> argument3,
                              CommonArgument<T4, C> argument4,
                              CommonArgument<T5, C> argument5,
                              CommonArgument<T6, C> argument6,
                              CommonArgument<T7, C> argument7) {
        super(delegate);
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.argument3 = argument3;
        this.argument4 = argument4;
        this.argument5 = argument5;
        this.argument6 = argument6;
        this.argument7 = argument7;
    }

    public HeptArgumentBranch<T1, T2, T3, T4, T5, T6, T7, C, T> execute(@NotNull OctoConsumer<T1, T2, T3, T4, T5, T6, T7, C> action) {
        super.execute(ctx -> action.accept(ctx.getParsedArg(argument1),
                                           ctx.getParsedArg(argument2),
                                           ctx.getParsedArg(argument3),
                                           ctx.getParsedArg(argument4),
                                           ctx.getParsedArg(argument5),
                                           ctx.getParsedArg(argument6),
                                           ctx.getParsedArg(argument7),
                                           ctx));
        return this;
    }

    @Override
    public HeptArgumentBranch<T1, T2, T3, T4, T5, T6, T7, C, T> execute(@Nullable CommandHandler<C> action) {
        super.execute(action);
        return this;
    }

    @Override
    public HeptArgumentBranch<T1, T2, T3, T4, T5, T6, T7, C, T> description(@NotNull String description) {
        super.description(description);
        return this;
    }

    public HeptArgumentBranch<T1, T2, T3, T4, T5, T6, T7, C, T> child(@NotNull HeptFunction<CommonArgument<T1, C>, CommonArgument<T2, C>, CommonArgument<T3, C>, CommonArgument<T4, C>, CommonArgument<T5, C>, CommonArgument<T6, C>, CommonArgument<T7, C>, T> factory) {
        child(factory.apply(argument1, argument2, argument3, argument4, argument5, argument6, argument7));
        return this;
    }

    public <S> RequiredHeptArgumentBranch<S, T1, T2, T3, T4, T5, T6, T7, C, T> require(@NotNull Extractor<C, S> extractor) {
        return new RequiredHeptArgumentBranch<>(extractor,
                                                this::execute,
                                                children -> delegate.addChildren(children),
                                                argument1,
                                                argument2,
                                                argument3,
                                                argument4,
                                                argument5,
                                                argument6,
                                                argument7);
    }
}
