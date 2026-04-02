package net.kunmc.lab.commandlib.exception;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;

import java.util.function.Consumer;

public class ArgumentValidationException extends ArgumentParseException {
    public ArgumentValidationException() {
        this("不正な値です");
    }

    public <C extends AbstractCommandContext<?, ?>> ArgumentValidationException(CommonArgument<?, C> argument,
                                                                                C ctx,
                                                                                String incorrectInput) {
        super(argument, ctx, incorrectInput);
    }

    public ArgumentValidationException(String message, String... messages) {
        this(ctx -> {
            ctx.sendFailure(message);
            for (String s : messages) {
                ctx.sendFailure(s);
            }
        });
    }

    public ArgumentValidationException(Consumer<AbstractCommandContext<?, ?>> sendMessages) {
        super(sendMessages);
    }
}
