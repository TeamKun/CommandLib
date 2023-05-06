package net.kunmc.lab.commandlib.exception;

import net.kunmc.lab.commandlib.AbstractCommandContext;

import java.util.function.Consumer;

public final class InvalidArgumentException extends RuntimeException {
    private final Consumer<AbstractCommandContext<?, ?>> sendMessages;

    public InvalidArgumentException(String message, String... messages) {
        this(ctx -> {
            ctx.sendFailure(message);
            for (String s : messages) {
                ctx.sendFailure(s);
            }
        });
    }

    public InvalidArgumentException(Consumer<AbstractCommandContext<?, ?>> sendMessages) {
        this.sendMessages = sendMessages;
    }

    public IncorrectArgumentInputException toIncorrectArgumentInputException() {
        return new IncorrectArgumentInputException(sendMessages);
    }
}
