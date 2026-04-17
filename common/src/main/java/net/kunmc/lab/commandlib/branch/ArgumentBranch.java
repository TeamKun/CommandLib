package net.kunmc.lab.commandlib.branch;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonCommand;
import net.kunmc.lab.commandlib.command.CommandHandler;
import net.kunmc.lab.commandlib.command.Extractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ArgumentBranch<C extends AbstractCommandContext<?, ?>, T extends CommonCommand<C, ?, T>> {
    protected final ArgumentBranchDelegate<C, T> delegate;

    public ArgumentBranch(@NotNull ArgumentBranchDelegate<C, T> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    public ArgumentBranch<C, T> description(@NotNull String description) {
        delegate.description(Objects.requireNonNull(description));
        return this;
    }

    public ArgumentBranch<C, T> execute(@Nullable CommandHandler<C> action) {
        delegate.execute(action);
        return this;
    }

    @SafeVarargs
    public final ArgumentBranch<C, T> child(@NotNull T child, @NotNull T... children) {
        List<T> list = new ArrayList<>();
        list.add(child);
        Collections.addAll(list, children);
        delegate.addChildren(list);
        return this;
    }

    public <S> RequiredArgumentBranch<S, C, T> require(@NotNull Extractor<C, S> extractor) {
        return new RequiredArgumentBranch<>(Objects.requireNonNull(extractor),
                                            this::execute,
                                            children -> delegate.addChildren(children));
    }
}
