package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.ArgumentParticle;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.CraftParticle;

public class ParticleArgument extends Argument<Particle> {
    public ParticleArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentParticle.a());
    }

    @Override
    public Particle parse(CommandContext<CommandListenerWrapper> ctx) {
        return CraftParticle.toBukkit(ArgumentParticle.a(ctx, name));
    }
}
