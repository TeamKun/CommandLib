package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentParticle;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.CraftParticle;

import java.util.function.Consumer;

public class ParticleArgument extends Argument<Particle> {
    public ParticleArgument(String name) {
        this(name, option -> {
        });
    }

    public ParticleArgument(String name, Consumer<Option<Particle, CommandContext>> options) {
        super(name, ArgumentParticle.a());
        setOptions(options);
    }

    @Override
    public Particle cast(Object parsedArgument) {
        return ((Particle) parsedArgument);
    }

    @Override
    protected Particle parseImpl(CommandContext ctx) throws IncorrectArgumentInputException {
        return CraftParticle.toBukkit(ArgumentParticle.a(ctx.getHandle(), name));
    }
}
