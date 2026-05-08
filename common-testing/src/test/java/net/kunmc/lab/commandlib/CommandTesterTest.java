package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.CommonStringArgument;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandTesterTest {
    @Test
    void executesCommonCommand() throws Exception {
        CommandTester tester = new CommandTester(new TestCommand("hello") {{
            argument(new CommonStringArgument<>("name")).execute(ctx -> ctx.sendMessage("Hello " + ctx.getInput("name")));
        }});

        FakeSender sender = FakeSender.console();

        tester.execute("hello Alex", sender);

        assertThat(sender.getSentMessageTexts()).containsExactly("Hello Alex");
    }

    @Test
    void filtersByPermission() {
        CommandTester tester = new CommandTester(new TestCommand("secure") {{
            execute(ctx -> ctx.sendMessage("ok"));
        }}, "test.command");

        assertThatThrownBy(() -> tester.execute("secure", FakeSender.unknown())).isInstanceOf(RuntimeException.class);
    }

    @Test
    void suggestsLiterals() {
        CommandTester tester = new CommandTester(new TestCommand("root") {{
            addChildren(new TestCommand("child"));
        }});

        List<String> suggestions = tester.suggestions("root ", FakeSender.console())
                                         .join()
                                         .getList()
                                         .stream()
                                         .map(com.mojang.brigadier.suggestion.Suggestion::getText)
                                         .collect(Collectors.toList());

        assertThat(suggestions).contains("child");
    }
}
