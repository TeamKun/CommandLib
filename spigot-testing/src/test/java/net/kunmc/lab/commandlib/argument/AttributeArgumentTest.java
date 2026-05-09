package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.FakeSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AttributeArgumentTest {
    @Test
    void attribute_is_parsed_case_insensitively() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("attribute") {{
            argument(new AttributeArgument("value")).execute((attribute, ctx) -> ctx.sendMessage(attribute.name()));
        }}, "test.command")) {
            tester.execute("attribute generic_max_health", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("GENERIC_MAX_HEALTH");
    }
}
