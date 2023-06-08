package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.arguments.PotionArgument;
import net.minecraft.potion.Effect;

import java.util.function.Consumer;

public class EffectArgument extends Argument<Effect> {
    public EffectArgument(String name) {
        this(name, option -> {
        });
    }

    public EffectArgument(String name, Consumer<Option<Effect, CommandContext>> options) {
        super(name, PotionArgument.mobEffect());
        setOptions(options);
    }

    @Override
    public Effect cast(Object parsedArgument) {
        return ((Effect) parsedArgument);
    }

    @Override
    protected Effect parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return PotionArgument.getMobEffect(ctx.getHandle(), name());
    }
}
