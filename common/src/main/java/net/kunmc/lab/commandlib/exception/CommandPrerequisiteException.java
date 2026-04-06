package net.kunmc.lab.commandlib.exception;

import net.kunmc.lab.commandlib.AbstractCommandContext;

import java.util.function.Consumer;

public final class CommandPrerequisiteException extends Exception {
    private final Consumer<AbstractCommandContext<?, ?>> sendMessage;

    public CommandPrerequisiteException() {
        this("You don't have permission to execute this command.");
    }

    public CommandPrerequisiteException(String message, String... additionalMessages) {
        this.sendMessage = ctx -> {
            ctx.sendFailure(message);
            for (String additionalMessage : additionalMessages) {
                ctx.sendFailure(additionalMessage);
            }
        };
    }

    public CommandPrerequisiteException(Consumer<AbstractCommandContext<?, ?>> sendMessage) {
        this.sendMessage = sendMessage;
    }

    public void sendMessage(AbstractCommandContext<?, ?> ctx) {
        sendMessage.accept(ctx);
    }
}
