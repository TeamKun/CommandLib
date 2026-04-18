package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentParticle;
import net.kunmc.lab.commandlib.util.nms.resources.NMSCraftParticle;
import org.bukkit.Particle;

public class ParticleArgument extends Argument<Particle, ParticleArgument> {
    public ParticleArgument(String name) {
        super(name,
              NMSArgumentParticle.create()
                                 .argument());
    }

    @Override
    public Particle cast(Object parsedArgument) {
        return ((Particle) parsedArgument);
    }

    @Override
    protected Particle parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return NMSCraftParticle.create()
                               .toBukkit(NMSArgumentParticle.create()
                                                            .parse(ctx.getHandle(), name()));
    }
}
