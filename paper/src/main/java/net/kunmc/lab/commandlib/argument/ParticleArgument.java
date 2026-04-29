package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.Particle;

@SuppressWarnings("UnstableApiUsage")
public class ParticleArgument extends Argument<Particle, ParticleArgument> {
    public ParticleArgument(String name) {
        super(name, ArgumentTypes.resource(RegistryKey.PARTICLE_TYPE));
    }

    @Override
    public Particle cast(Object parsedArgument) {
        return (Particle) parsedArgument;
    }

    @Override
    protected Particle parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return ctx.getHandle()
                  .getArgument(name(), Particle.class);
    }
}
