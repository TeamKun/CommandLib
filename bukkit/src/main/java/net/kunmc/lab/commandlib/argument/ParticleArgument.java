package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentParticle;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.CraftParticle;

import java.util.function.Consumer;

public class ParticleArgument extends Argument<Particle> {
    public ParticleArgument(String name) {
        this(name, option -> {
        });
    }

    public ParticleArgument(String name, Consumer<Option<Particle>> options) {
        super(name, ArgumentParticle.a());
        setOptions(options);
    }

    @Override
    protected Particle cast(Object parsedArgument) {
        return ((Particle) parsedArgument);
    }

    @Override
    public Particle parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        return CraftParticle.toBukkit(ArgumentParticle.a(ctx, name));
    }
}
