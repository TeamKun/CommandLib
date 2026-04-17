package net.kunmc.lab.commandlib.branch;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonCommand;
import net.kunmc.lab.commandlib.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ArgumentBranchDelegate<C extends AbstractCommandContext<?, ?>, T extends CommonCommand<C, ?, T>> {
    void description(@NotNull String description);

    void execute(@Nullable CommandExecutor<C> action);

    void addChildren(@NotNull Collection<? extends T> children);
}
