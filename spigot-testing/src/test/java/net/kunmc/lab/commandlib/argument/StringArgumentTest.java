package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.FakeSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringArgumentTest {
    @Test
    void single_word_is_parsed() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("say") {{
            argument(new StringArgument("message")).execute((message, ctx) -> {
                ctx.sendMessage(">" + message);
            });
        }}, "test.command")) {
            tester.execute("say hello", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly(">hello");
    }

    @Test
    void quoted_phrase_is_parsed() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("say") {{
            argument(new StringArgument("message")).execute((message, ctx) -> {
                ctx.sendMessage(">" + message);
            });
        }}, "test.command")) {
            tester.execute("say \"hello world\"", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly(">hello world");
    }
}
