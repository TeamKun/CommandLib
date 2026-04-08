package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.FloatArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FloatArgumentTest {
    @Test
    void float_value_is_parsed() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("set") {{
            argument(new FloatArgument("value"), (value, ctx) -> {
                ctx.sendMessage("value=" + value);
            });
        }}, "test.command")) {
            tester.execute("set 1.5", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("value=1.5");
    }
}
