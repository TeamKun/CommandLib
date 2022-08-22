package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.TranslationTextComponent;

public class TeamArgument extends Argument<ScorePlayerTeam> {
    public TeamArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, net.minecraft.command.arguments.TeamArgument.team());
    }

    @Override
    public ScorePlayerTeam parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        try {
            return net.minecraft.command.arguments.TeamArgument.getTeam(ctx, name);
        } catch (CommandSyntaxException e) {
            throw new IncorrectArgumentInputException(new TranslationTextComponent("team.notFound", ctx.getArgument(name, String.class)));
        }
    }
}
