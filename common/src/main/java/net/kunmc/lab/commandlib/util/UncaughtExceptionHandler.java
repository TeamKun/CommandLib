package net.kunmc.lab.commandlib.util;

import net.kunmc.lab.commandlib.AbstractCommandContext;

@FunctionalInterface
public interface UncaughtExceptionHandler<C extends AbstractCommandContext<?, ?>> {
    void uncaughtException(Throwable e, C ctx);
}
