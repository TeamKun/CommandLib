package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.PotionArgument;
import net.minecraft.potion.Effect;

import java.util.function.Consumer;

public class EffectArgument extends Argument<Effect> {
    public EffectArgument(String name) {
        this(name, option -> {
        });
    }

    public EffectArgument(String name, Consumer<Option<Effect>> options) {
        super(name, PotionArgument.mobEffect());
        setOptions(options);
    }

    @Override
    public Effect cast(Object parsedArgument) {
        return ((Effect) parsedArgument);
    }

    @Override
    public Effect parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return PotionArgument.getMobEffect(ctx, name);
    }
}
