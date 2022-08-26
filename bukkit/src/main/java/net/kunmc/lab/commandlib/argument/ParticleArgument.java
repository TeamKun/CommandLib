package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentParticle;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_16_R3.CraftParticle;

import java.util.function.Predicate;

public class ParticleArgument extends Argument<Particle> {
    private final Predicate<? super Particle> filter;

    public ParticleArgument(String name, SuggestionAction suggestionAction, Predicate<? super Particle> filter, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentParticle.a());
        this.filter = filter;
    }

    @Override
    public Particle parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        Particle particle = CraftParticle.toBukkit(ArgumentParticle.a(ctx, name));
        if (filter == null || filter.test(particle)) {
            return particle;
        }
        throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
    }
}
