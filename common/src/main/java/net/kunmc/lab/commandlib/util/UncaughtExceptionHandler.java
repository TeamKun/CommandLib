package net.kunmc.lab.commandlib.util;

import net.kunmc.lab.commandlib.CommonCommandContext;

@FunctionalInterface
public interface UncaughtExceptionHandler<C extends CommonCommandContext<?, ?>> {
    void uncaughtException(Throwable e, C ctx);
}
