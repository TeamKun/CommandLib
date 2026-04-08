package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.UnparsedArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnparsedArgumentTest {
    @Test
    void raw_input_is_returned() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(() -> new Command("raw") {{
            argument(new UnparsedArgument("text"), (text, ctx) -> {
                ctx.sendMessage("raw:" + text);
            });
        }}, "test.command")) {
            tester.execute("raw hello world", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("raw:hello world");
    }
}
