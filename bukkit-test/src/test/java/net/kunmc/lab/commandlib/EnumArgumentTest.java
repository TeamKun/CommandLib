package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.EnumArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnumArgumentTest {
    enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    @Test
    void enum_constant_is_resolved_case_insensitively() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("go") {{
            argument(new EnumArgument<>("dir", Direction.class), (dir, ctx) -> {
                ctx.sendMessage("going " + dir.name());
            });
        }}, "test.command")) {
            tester.execute("go north", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("going NORTH");
    }

    @Test
    void unknown_enum_constant_sends_failure_message() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("go") {{
            argument(new EnumArgument<>("dir", Direction.class), (dir, ctx) -> {
                ctx.sendMessage("going " + dir.name());
            });
        }}, "test.command")) {
            tester.execute("go up", sender);
        }

        assertThat(sender.getSentMessageTexts()).isNotEmpty();
        assertThat(sender.getSentMessageTexts()).doesNotContain("going NORTH");
    }
}
