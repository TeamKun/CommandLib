package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.particles.IParticleData;

import java.util.function.Consumer;

public class ParticleArgument extends Argument<IParticleData> {
    public ParticleArgument(String name) {
        this(name, option -> {
        });
    }

    public ParticleArgument(String name, Consumer<Option<IParticleData>> options) {
        super(name, net.minecraft.command.arguments.ParticleArgument.particle());
        setOptions(options);
    }

    @Override
    protected IParticleData cast(Object parsedArgument) {
        return ((IParticleData) parsedArgument);
    }

    @Override
    public IParticleData parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        return net.minecraft.command.arguments.ParticleArgument.getParticle(ctx, name);
    }
}
