package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.FakeSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BiomeArgumentTest {
    @Test
    void biome_is_resolved_by_enum_name() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(new Command("biome") {{
            argument(new BiomeArgument("biome")).execute((biome, ctx) -> ctx.sendMessage(biome.name()));
        }}, "test.command")) {
            tester.execute("biome plains", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("PLAINS");
    }
}
