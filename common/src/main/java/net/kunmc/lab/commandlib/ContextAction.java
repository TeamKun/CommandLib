package net.kunmc.lab.commandlib;

import java.util.function.Consumer;

public interface ContextAction<C extends AbstractCommandContext<?, ?>> extends Consumer<C> {
    default int executeWithStackTrace(C ctx) {
        try {
            accept(ctx);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            ctx.sendFailure("An unexpected error occurred trying to execute that command.");
            ctx.sendFailure("Check the console for details.");
            return 0;
        }
    }
}
