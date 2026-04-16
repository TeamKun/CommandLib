package net.kunmc.lab.commandlib.branch;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.CommonCommand;
import net.kunmc.lab.commandlib.ContextAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class UnaryArgumentBranch<T1, C extends AbstractCommandContext<?, ?>, T extends CommonCommand<C, ?, T>> extends ArgumentBranch<C, T> {
    private final CommonArgument<T1, C> argument1;

    public UnaryArgumentBranch(ArgumentBranchDelegate<C, T> delegate, CommonArgument<T1, C> argument1) {
        super(delegate);
        this.argument1 = argument1;
    }

    public UnaryArgumentBranch<T1, C, T> execute(@NotNull BiConsumer<T1, C> action) {
        super.execute(ctx -> action.accept(ctx.getParsedArg(argument1), ctx));
        return this;
    }

    @Override
    public UnaryArgumentBranch<T1, C, T> execute(@Nullable ContextAction<C> action) {
        super.execute(action);
        return this;
    }

    @Override
    public UnaryArgumentBranch<T1, C, T> description(@NotNull String description) {
        super.description(description);
        return this;
    }

    public UnaryArgumentBranch<T1, C, T> child(@NotNull Function<CommonArgument<T1, C>, T> factory) {
        child(factory.apply(argument1));
        return this;
    }
}
