package net.kunmc.lab.commandlib;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class TestCommandContext extends AbstractCommandContext<Object, String> {
    private static TestCommandContext latest;
    private final List<String> messages = new ArrayList<>();

    TestCommandContext(com.mojang.brigadier.context.CommandContext<Object> ctx) {
        super(ctx);
        latest = this;
    }

    static TestCommandContext latest() {
        return latest;
    }

    static void clearLatest() {
        latest = null;
    }

    @Override
    public void sendMessage(String message) {
        messages.add(String.valueOf(message));
    }

    @Override
    public void sendSuccess(String message) {
        messages.add(String.valueOf(message));
    }

    @Override
    public void sendWarn(String message) {
        messages.add(String.valueOf(message));
    }

    @Override
    public void sendFailure(String message) {
        messages.add(String.valueOf(message));
    }

    @Override
    public void sendMessageWithOption(String message, Consumer<MessageOption> options) {
        messages.add(String.valueOf(message));
    }

    @Override
    public void sendComponent(String component) {
        messages.add(component);
    }

    List<String> messages() {
        return List.copyOf(messages);
    }
}
