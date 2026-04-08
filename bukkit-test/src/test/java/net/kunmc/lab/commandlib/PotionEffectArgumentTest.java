package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.PotionEffectArgument;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class PotionEffectArgumentTest {
    @Test
    void effect_name_resolves_to_potion_effect() {
        FakeSender sender = FakeSender.player("Alice");
        PotionEffectType mockEffectType = Mockito.mock(PotionEffectType.class);
        Mockito.when(mockEffectType.getName())
               .thenReturn("SPEED");
        Mockito.when(mockEffectType.createEffect(1, 0))
               .thenReturn(new PotionEffect(mockEffectType, 1, 0));

        try (MockedStatic<PotionEffectType> potionStatic = Mockito.mockStatic(PotionEffectType.class);
             CommandTester tester = new CommandTester(() -> new Command("effect") {{
                 argument(new PotionEffectArgument("type"), (effect, ctx) -> {
                     ctx.sendMessage(effect.getType()
                                           .getName());
                 });
             }}, "test.command")) {
            potionStatic.when(() -> PotionEffectType.getByName("SPEED"))
                        .thenReturn(mockEffectType);
            tester.execute("effect SPEED", sender);
        }

        assertThat(sender.getSentMessageTexts()).containsExactly("SPEED");
    }
}
