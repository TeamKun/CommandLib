package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.FakeSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SoundArgumentTest {
    @Test
    void sound_is_parsed_case_insensitively() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("sound") {{
            argument(new SoundArgument("value")).execute((sound, ctx) -> ctx.sendMessage(sound.name()));
        }}, "test.command")) {
            tester.execute("sound block_anvil_land", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("BLOCK_ANVIL_LAND");
    }
}
