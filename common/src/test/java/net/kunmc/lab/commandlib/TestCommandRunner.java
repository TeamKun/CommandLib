package net.kunmc.lab.commandlib;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.List;

final class TestCommandRunner {
    private final CommandDispatcher<Object> dispatcher = new CommandDispatcher<>();

    TestCommandRunner(TestCommand command) {
        new CommandNodeCreator<>(List.of(command), "test.command").build()
                                                                  .forEach(dispatcher.getRoot()::addChild);
    }

    TestCommandContext execute(String input) throws CommandSyntaxException {
        TestCommandContext.clearLatest();
        dispatcher.execute(input, new Object());
        return TestCommandContext.latest();
    }
}
