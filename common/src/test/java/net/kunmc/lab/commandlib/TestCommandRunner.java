package net.kunmc.lab.commandlib;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;

import java.util.List;
import java.util.stream.Collectors;

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

    List<String> suggest(String input) {
        Suggestions suggestions = dispatcher.getCompletionSuggestions(dispatcher.parse(input, new Object()))
                                            .join();
        return suggestions.getList()
                          .stream()
                          .map(com.mojang.brigadier.suggestion.Suggestion::getText)
                          .collect(Collectors.toList());
    }
}
