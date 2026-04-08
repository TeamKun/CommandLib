package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.ItemStackArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemStackArgumentTest {
    @Test
    void material_name_resolves_to_item_stack() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(() -> new Command("give") {{
            argument(new ItemStackArgument("item"), (item, ctx) -> {
                ctx.sendMessage(item.getType()
                                    .name());
            });
        }}, "test.command")) {
            tester.execute("give stone", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("STONE");
    }

    @Test
    void unknown_material_falls_back_to_air() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(() -> new Command("give") {{
            argument(new ItemStackArgument("item"), (item, ctx) -> {
                ctx.sendMessage(item.getType()
                                    .name());
            });
        }}, "test.command")) {
            tester.execute("give not_a_real_material", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("AIR");
    }
}
