package net.kunmc.lab.commandlib;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;
import net.kunmc.lab.commandlib.util.UncaughtExceptionHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

final class CommandExecutor<S, C extends AbstractCommandContext<S, ?>> implements Command<S> {
    private final PlatformAdapter<S, ?, C, ?, ?> platformAdapter;
    private final Arguments<C> arguments;
    private final List<CommandOption<?, C>> options;
    private final Prerequisite<C> prerequisite;
    private final ContextAction<C> helpAction;
    private final Predicate<C> preprocess;
    private final ContextAction<C> contextAction;
    private final List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers;


    CommandExecutor(PlatformAdapter<S, ?, C, ?, ?> platformAdapter,
                    @Nullable Arguments<C> arguments,
                    List<CommandOption<?, C>> options,
                    Prerequisite<C> prerequisite,
                    ContextAction<C> helpAction,
                    Predicate<C> preprocess,
                    ContextAction<C> contextAction,
                    List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers) {
        this.platformAdapter = platformAdapter;
        this.arguments = arguments;
        this.options = options;
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
                parseOptions(ctx);

                if (arguments != null) {
                    try {
                        arguments.parse(ctx);
                    } catch (ArgumentParseException e) {
                        e.sendMessage(ctx);
                        return 1;
                    }
                }

                try {
                    prerequisite.check(ctx);
                } catch (CommandPrerequisiteException e) {
                    e.sendMessage(ctx);
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

    private void parseOptions(C ctx) {
        Map<String, CommandOption<?, C>> longOptionByToken = new HashMap<>();
        Map<Character, CommandOption<?, C>> shortOptionByToken = new HashMap<>();

        for (CommandOption<?, C> option : options) {
            longOptionByToken.put("--" + option.name(), option);
            if (option.shortName() != null) {
                shortOptionByToken.put(option.shortName(), option);
            }
        }

        ctx.getHandle()
           .getNodes()
           .stream()
           .map(x -> x.getNode()
                      .getName())
           .filter(x -> x.startsWith("-"))
           .forEach(token -> {
               CommandOption<?, C> longOption = longOptionByToken.get(token);
               if (longOption != null) {
                   if (!longOption.hasValue()) {
                       ctx.setOptionValue(longOption, true);
                   }
                   return;
               }

               if (token.startsWith("--")) {
                   return;
               }

               for (int i = 1; i < token.length(); i++) {
                   CommandOption<?, C> option = shortOptionByToken.get(token.charAt(i));
                   if (option != null && !option.hasValue()) {
                       ctx.setOptionValue(option, true);
                   }
               }
           });

        ctx.getHandle()
           .getNodes()
           .stream()
           .map(x -> x.getNode()
                      .getName())
           .filter(x -> x.startsWith(CommandOption.INTERNAL_NAME_PREFIX))
           .forEach(name -> options.stream()
                                   .filter(CommandOption::hasValue)
                                   .filter(x -> x.internalName()
                                                 .equals(name))
                                   .findFirst()
                                   .ifPresent(x -> ctx.setOptionValue(x, x.parse(ctx))));
    }
}
