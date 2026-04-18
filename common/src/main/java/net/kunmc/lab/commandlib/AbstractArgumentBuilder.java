package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract class AbstractArgumentBuilder<C extends AbstractCommandContext<?, ?>, T extends AbstractArgumentBuilder<C, T>> {
    private final List<CommonArgument<?, C, ?>> arguments = new ArrayList<>();
    private CommandExecutor<C> executor = null;

    @SuppressWarnings("unchecked")
    public final T argument(@NotNull CommonArgument<?, C, ?> argument) {
        Objects.requireNonNull(argument);
        boolean duplicated = arguments.stream()
                                      .anyMatch(x -> x.name()
                                                      .equals(argument.name()));
        if (duplicated) {
            throw new IllegalArgumentException("Duplicate argument name: " + argument.name());
        }
        arguments.add(argument);
        return (T) this;
    }

    public final void execute(@Nullable CommandExecutor<C> executor) {
        this.executor = executor;
    }

    final List<CommonArgument<?, C, ?>> build() {
        if (!arguments.isEmpty()) {
            CommonArgument<?, C, ?> last = arguments.get(arguments.size() - 1);
            if (last.executor() == null) {
                last.execute(executor);
            }
        }
        return arguments;
    }
}
