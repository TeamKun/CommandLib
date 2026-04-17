package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;

@FunctionalInterface
public interface CommandHandler<C extends AbstractCommandContext<?, ?>> {
    void accept(C ctx) throws CommandPrerequisiteException;
}
