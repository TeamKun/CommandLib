package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.FakeSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatColorArgumentTest {
    @Test
    void chat_color_is_parsed_case_insensitively() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("color") {{
            argument(new ChatColorArgument("value")).execute((color, ctx) -> ctx.sendMessage(color.name()));
        }}, "test.command")) {
            tester.execute("color dark_red", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("DARK_RED");
    }
}
