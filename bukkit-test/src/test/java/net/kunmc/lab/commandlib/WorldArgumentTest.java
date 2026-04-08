package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.WorldArgument;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class WorldArgumentTest {
    @Test
    void world_is_resolved_by_name() {
        FakeSender sender = FakeSender.player("Alice");
        World mockWorld = Mockito.mock(World.class);
        Mockito.when(mockWorld.getName())
               .thenReturn("nether");

        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class); CommandTester tester = new CommandTester(
                new Command("tp") {{
                    argument(new WorldArgument("world"), (world, ctx) -> {
                        ctx.sendMessage(world.getName());
                    });
                }},
                "test.command")) {
            bukkit.when(() -> Bukkit.getWorld("nether"))
                  .thenReturn(mockWorld);
            tester.execute("tp nether", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("nether");
    }

    @Test
    void unknown_world_sends_failure_message() {
        FakeSender sender = FakeSender.player("Alice");

        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class); CommandTester tester = new CommandTester(
                new Command("tp") {{
                    argument(new WorldArgument("world"), (world, ctx) -> {
                        ctx.sendMessage(world.getName());
                    });
                }},
                "test.command")) {
            bukkit.when(() -> Bukkit.getWorld("unknown"))
                  .thenReturn(null);
            tester.execute("tp unknown", sender);
        }

        assertThat(sender.getSentMessageTexts()).isNotEmpty();
        assertThat(sender.getSentMessageTexts()).doesNotContain("unknown");
    }
}
