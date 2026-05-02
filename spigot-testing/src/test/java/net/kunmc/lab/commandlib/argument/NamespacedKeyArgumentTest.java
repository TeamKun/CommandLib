package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.FakeSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NamespacedKeyArgumentTest {
    @Test
    void namespaced_key_is_parsed() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(() -> new Command("key") {{
            argument(new NamespacedKeyArgument("value")).execute((key, ctx) -> ctx.sendMessage(key.toString()));
        }}, "test.command")) {
            tester.execute("key commandlib:example/path", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("commandlib:example/path");
    }

    @Test
    void missing_namespace_defaults_to_minecraft() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(() -> new Command("key") {{
            argument(new NamespacedKeyArgument("value")).execute((key, ctx) -> ctx.sendMessage(key.toString()));
        }}, "test.command")) {
            tester.execute("key stone", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("minecraft:stone");
    }
}
