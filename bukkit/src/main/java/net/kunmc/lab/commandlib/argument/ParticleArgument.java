package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentParticle;
import net.kunmc.lab.commandlib.util.nms.resources.NMSCraftParticle;
import org.bukkit.Particle;

import java.util.function.Consumer;

public class ParticleArgument extends Argument<Particle> {
    public ParticleArgument(String name) {
        this(name, option -> {
        });
    }

    public ParticleArgument(String name, Consumer<Option<Particle, CommandContext>> options) {
        super(name,
              NMSArgumentParticle.create()
                                 .argument());
        applyOptions(options);
    }

    @Override
    public Particle cast(Object parsedArgument) {
        return ((Particle) parsedArgument);
    }

    @Override
    protected Particle parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return NMSCraftParticle.create()
                               .toBukkit(NMSArgumentParticle.create()
                                                            .parse(ctx.getHandle(), name()));
    }
}
