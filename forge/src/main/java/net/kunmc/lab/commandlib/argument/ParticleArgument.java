package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.particles.IParticleData;

public class ParticleArgument extends Argument<IParticleData> {
    public ParticleArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, net.minecraft.command.arguments.ParticleArgument.particle());
    }

    @Override
    public IParticleData parse(CommandContext<CommandSource> ctx) {
        return net.minecraft.command.arguments.ParticleArgument.getParticle(ctx, name);
    }
}
