package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.PotionArgument;
import net.minecraft.potion.Effect;

public class EffectArgument extends Argument<Effect> {
    public EffectArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, PotionArgument.mobEffect());
    }

    @Override
    public Effect parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        try {
            return PotionArgument.getMobEffect(ctx, name);
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
