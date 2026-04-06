package net.kunmc.lab.commandlib.exception;

import net.kunmc.lab.commandlib.AbstractCommandContext;

import java.util.function.Consumer;

public class ArgumentValidationException extends ArgumentParseException {
    public static <C extends AbstractCommandContext<?, ?>> ArgumentValidationException ofIncorrectInput(String argumentName,
                                                                                                        C ctx,
                                                                                                        String incorrectInput) {
        return new ArgumentValidationException(buildIncorrectInputMessage(argumentName, ctx, incorrectInput));
    }

    public ArgumentValidationException() {
        super("Invalid value.");
    }

    public ArgumentValidationException(String message, String... messages) {
        super(message, messages);
    }

    public ArgumentValidationException(Consumer<AbstractCommandContext<?, ?>> sendMessages) {
        super(sendMessages);
    }
}
