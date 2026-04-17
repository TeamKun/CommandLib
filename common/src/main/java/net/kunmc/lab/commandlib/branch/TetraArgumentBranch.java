package net.kunmc.lab.commandlib.branch;

import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.command.Extractor;
import net.kunmc.lab.commandlib.command.CommandHandler;
import net.kunmc.lab.commandlib.*;
import net.kunmc.lab.commandlib.util.function.QuintConsumer;
import net.kunmc.lab.commandlib.util.function.TetraFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TetraArgumentBranch<T1, T2, T3, T4, C extends AbstractCommandContext<?, ?>, T extends CommonCommand<C, ?, T>> extends ArgumentBranch<C, T> {
    private final CommonArgument<T1, C> argument1;
    private final CommonArgument<T2, C> argument2;
    private final CommonArgument<T3, C> argument3;
    private final CommonArgument<T4, C> argument4;

    public TetraArgumentBranch(ArgumentBranchDelegate<C, T> delegate,
                               CommonArgument<T1, C> argument1,
                               CommonArgument<T2, C> argument2,
                               CommonArgument<T3, C> argument3,
                               CommonArgument<T4, C> argument4) {
        super(delegate);
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.argument3 = argument3;
        this.argument4 = argument4;
    }

    public TetraArgumentBranch<T1, T2, T3, T4, C, T> execute(@NotNull QuintConsumer<T1, T2, T3, T4, C> action) {
        super.execute(ctx -> action.accept(ctx.getArgument(argument1),
                                           ctx.getArgument(argument2),
                                           ctx.getArgument(argument3),
                                           ctx.getArgument(argument4),
                                           ctx));
        return this;
    }

    @Override
    public TetraArgumentBranch<T1, T2, T3, T4, C, T> execute(@Nullable CommandHandler<C> action) {
        super.execute(action);
        return this;
    }

    @Override
    public TetraArgumentBranch<T1, T2, T3, T4, C, T> description(@NotNull String description) {
        super.description(description);
        return this;
    }

    public TetraArgumentBranch<T1, T2, T3, T4, C, T> child(@NotNull TetraFunction<CommonArgument<T1, C>, CommonArgument<T2, C>, CommonArgument<T3, C>, CommonArgument<T4, C>, T> factory) {
        child(factory.apply(argument1, argument2, argument3, argument4));
        return this;
    }

    public <S> RequiredTetraArgumentBranch<S, T1, T2, T3, T4, C, T> require(@NotNull Extractor<C, S> extractor) {
        return new RequiredTetraArgumentBranch<>(extractor,
                                                 this::execute,
                                                 children -> delegate.addChildren(children),
                                                 argument1,
                                                 argument2,
                                                 argument3,
                                                 argument4);
    }
}
