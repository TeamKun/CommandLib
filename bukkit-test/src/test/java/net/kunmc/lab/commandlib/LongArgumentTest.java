package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.LongArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LongArgumentTest {
    @Test
    void long_value_is_parsed() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("set") {{
            argument(new LongArgument("value"), (value, ctx) -> {
                ctx.sendMessage("value=" + value);
            });
        }}, "test.command")) {
            tester.execute("set 9999999999", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("value=9999999999");
    }
}
