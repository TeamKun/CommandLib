package net.kunmc.lab.commandlib.util;

import net.kunmc.lab.commandlib.AbstractCommandContext;

@FunctionalInterface
public interface UncaughtExceptionHandler<S, C extends AbstractCommandContext<S, ?>> {
    void uncaughtException(Throwable e, C ctx);
}
