package net.kunmc.lab.commandlib.command;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;

@FunctionalInterface
public interface CommandHandler<C extends AbstractCommandContext<?, ?>> {
    void accept(C ctx) throws CommandPrerequisiteException;
}
