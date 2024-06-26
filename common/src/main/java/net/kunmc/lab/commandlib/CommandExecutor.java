package net.kunmc.lab.commandlib;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.UncaughtExceptionHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

final class CommandExecutor<S, C extends AbstractCommandContext<S, ?>> implements Command<S> {
    private final PlatformAdapter<S, ?, C, ?, ?> platformAdapter;
    private final Arguments<C> arguments;
    private final Predicate<C> prerequisite;
    private final ContextAction<C> helpAction;
    private final Predicate<C> preprocess;
    private final ContextAction<C> contextAction;
    private final List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers;


    CommandExecutor(PlatformAdapter<S, ?, C, ?, ?> platformAdapter,
                    @Nullable Arguments<C> arguments,
                    Predicate<C> prerequisite,
                    ContextAction<C> helpAction,
                    Predicate<C> preprocess,
                    ContextAction<C> contextAction,
                    List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers) {
        this.platformAdapter = platformAdapter;
        this.arguments = arguments;
        this.prerequisite = prerequisite;
        this.helpAction = helpAction;
        this.preprocess = preprocess;
        this.contextAction = contextAction;
        this.uncaughtExceptionHandlers = uncaughtExceptionHandlers;
    }

    @Override
    public int run(CommandContext<S> context) {
        try {
            C ctx = platformAdapter.createCommandContext(context);

            try {
                if (arguments != null) {
                    try {
                        arguments.parse(ctx);
                    } catch (IncorrectArgumentInputException e) {
                        e.sendMessage(ctx);
                        return 1;
                    }
                }

                if (!prerequisite.test(ctx)) {
                    return 0;
                }

                if (contextAction == null) {
                    return executeWithStackTrace(ctx, helpAction);
                }

                if (!preprocess.test(ctx)) {
                    return 0;
                }

                return executeWithStackTrace(ctx, contextAction);
            } catch (Throwable e) {
                e.printStackTrace();
                uncaughtExceptionHandlers.forEach(x -> x.uncaughtException(e, ctx));
                throw e;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    private int executeWithStackTrace(C ctx, ContextAction<C> contextAction) {
        try {
            contextAction.accept(ctx);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            ctx.sendFailure("An unexpected error occurred trying to execute that command.");
            ctx.sendFailure("Check the console for details.");
            return 0;
        }
    }
}
