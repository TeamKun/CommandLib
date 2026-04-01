package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;

@FunctionalInterface
public interface Prerequisite<C extends AbstractCommandContext<?, ?>> {
    void check(C ctx) throws CommandPrerequisiteException;

    default Prerequisite<C> and(Prerequisite<C> other) {
        return ctx -> {
            check(ctx);
            other.check(ctx);
        };
    }
}
