package net.kunmc.lab.commandlib.branch;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.CommonCommand;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.util.function.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public final class BiArgumentBranch<T1, T2, C extends AbstractCommandContext<?, ?>, T extends CommonCommand<C, ?, T>> extends ArgumentBranch<C, T> {
    private final CommonArgument<T1, C> argument1;
    private final CommonArgument<T2, C> argument2;

    public BiArgumentBranch(ArgumentBranchDelegate<C, T> delegate,
                            CommonArgument<T1, C> argument1,
                            CommonArgument<T2, C> argument2) {
        super(delegate);
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    public BiArgumentBranch<T1, T2, C, T> execute(@NotNull TriConsumer<T1, T2, C> action) {
        super.execute(ctx -> action.accept(ctx.getParsedArg(argument1), ctx.getParsedArg(argument2), ctx));
        return this;
    }

    @Override
    public BiArgumentBranch<T1, T2, C, T> execute(@Nullable ContextAction<C> action) {
        super.execute(action);
        return this;
    }

    @Override
    public BiArgumentBranch<T1, T2, C, T> description(@NotNull String description) {
        super.description(description);
        return this;
    }

    public BiArgumentBranch<T1, T2, C, T> child(@NotNull BiFunction<CommonArgument<T1, C>, CommonArgument<T2, C>, T> factory) {
        child(factory.apply(argument1, argument2));
        return this;
    }
}
