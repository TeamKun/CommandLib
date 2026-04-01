package net.kunmc.lab.commandlib.exception;

import net.kunmc.lab.commandlib.AbstractCommandContext;

import java.util.function.Consumer;

public final class CommandPrerequisiteException extends Exception {
    private final Consumer<AbstractCommandContext<?, ?>> sendMessage;

    public CommandPrerequisiteException() {
        this.sendMessage = ctx -> {
            ctx.sendFailure("権限がありません");
        };
    }

    public CommandPrerequisiteException(Consumer<AbstractCommandContext<?, ?>> sendMessage) {
        this.sendMessage = sendMessage;
    }

    public void sendMessage(AbstractCommandContext<?, ?> ctx) {
        sendMessage.accept(ctx);
    }
}
