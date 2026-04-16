package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.FakeSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnparsedArgumentTest {
    @Test
    void raw_input_is_returned() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(() -> new Command("raw") {{
            argument(new UnparsedArgument("text")).execute((text, ctx) -> {
                ctx.sendMessage("raw:" + text);
            });
        }}, "test.command")) {
            tester.execute("raw hello world", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("raw:hello world");
    }
}
