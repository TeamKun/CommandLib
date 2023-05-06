package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.particles.IParticleData;

import java.util.function.Consumer;

public class ParticleArgument extends Argument<IParticleData> {
    public ParticleArgument(String name) {
        this(name, option -> {
        });
    }

    public ParticleArgument(String name, Consumer<Option<IParticleData, CommandContext>> options) {
        super(name, net.minecraft.command.arguments.ParticleArgument.particle());
        setOptions(options);
    }

    @Override
    public IParticleData cast(Object parsedArgument) {
        return ((IParticleData) parsedArgument);
    }

    @Override
    protected IParticleData parseImpl(CommandContext ctx) throws IncorrectArgumentInputException {
        return net.minecraft.command.arguments.ParticleArgument.getParticle(ctx.getHandle(), name);
    }
}
