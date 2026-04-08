package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.ParticleArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticleArgumentTest {
    @Test
    void particle_is_resolved_by_name() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(() -> new Command("particle") {{
            argument(new ParticleArgument("type"), (particle, ctx) -> {
                ctx.sendMessage("particle=" + particle.name());
            });
        }}, "test.command")) {
            tester.execute("particle FLAME", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("particle=FLAME");
    }

    @Test
    void unknown_particle_causes_failure() {
        FakeSender sender = FakeSender.player("Alice");

        try (CommandTester tester = new CommandTester(() -> new Command("particle") {{
            argument(new ParticleArgument("type"), (particle, ctx) -> {
                ctx.sendMessage(particle.name());
            });
        }}, "test.command")) {
            assertThatThrownBy(() -> tester.execute("particle NOT_A_PARTICLE",
                                                    sender)).isInstanceOf(RuntimeException.class);
        }
    }
}
