package net.kunmc.lab.commandlib.command;

import net.kunmc.lab.commandlib.CommonCommandContext;
import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;

@FunctionalInterface
public interface Prerequisite<C extends CommonCommandContext<?, ?>> {
    void check(C ctx) throws CommandPrerequisiteException;

    default Prerequisite<C> and(Prerequisite<C> other) {
        return ctx -> {
            check(ctx);
            other.check(ctx);
        };
    }
}
