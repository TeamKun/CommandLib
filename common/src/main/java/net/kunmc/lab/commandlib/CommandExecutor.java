package net.kunmc.lab.commandlib;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.RootCommandNode;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;
import net.kunmc.lab.commandlib.util.UncaughtExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

final class CommandExecutor<S, C extends AbstractCommandContext<S, ?>> implements Command<S> {
    private final PlatformAdapter<S, ?, C, ?, ?> platformAdapter;
    private final List<Arguments<C>> argumentsList;
    private final List<CommandOption<?, C>> options;
    private final Prerequisite<C> prerequisite;
    private final CommandHandler<C> helpAction;
    private final Predicate<C> preprocess;
    private final CommandHandler<C> contextAction;
    private final List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers;

    CommandExecutor(PlatformAdapter<S, ?, C, ?, ?> platformAdapter,
                    List<Arguments<C>> argumentsList,
                    List<CommandOption<?, C>> options,
                    Prerequisite<C> prerequisite,
                    CommandHandler<C> helpAction,
                    Predicate<C> preprocess,
                    CommandHandler<C> contextAction,
                    List<UncaughtExceptionHandler<?, C>> uncaughtExceptionHandlers) {
        this.platformAdapter = platformAdapter;
        this.argumentsList = List.copyOf(argumentsList);
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
            C ctx = platformAdapter.createCommandContext(rootContext(context));

            try {
                try {
                    parseOptions(ctx);
                    validateOptions(ctx);
                } catch (ArgumentParseException e) {
                    e.sendMessage(ctx);
                    return 1;
                }

                for (Arguments<C> arguments : argumentsList) {
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

    private CommandContext<S> rootContext(CommandContext<S> context) {
        if (!(context.getRootNode() instanceof RootCommandNode)) {
            return context;
        }

        RootCommandNode<S> root = (RootCommandNode<S>) context.getRootNode();
        return new CommandDispatcher<>(root).parse(context.getInput(), context.getSource())
                                            .getContext()
                                            .build(context.getInput());
    }

    private int executeWithStackTrace(C ctx, CommandHandler<C> contextAction) {
        try {
            contextAction.accept(ctx);
            return 1;
        } catch (CommandPrerequisiteException e) {
            e.sendMessage(ctx);
            return 0;
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

    private void validateOptions(C ctx) throws ArgumentParseException {
        for (CommandOption<?, C> option : options) {
            option.validate(ctx);
        }
    }
}
