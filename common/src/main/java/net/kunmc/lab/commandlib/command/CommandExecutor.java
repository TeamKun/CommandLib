package net.kunmc.lab.commandlib.command;

import net.kunmc.lab.commandlib.CommonCommandContext;
import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;

@FunctionalInterface
public interface CommandExecutor<C extends CommonCommandContext<?, ?>> {
    void accept(C ctx) throws CommandPrerequisiteException;
}
