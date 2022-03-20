package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.command.CommandSource;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class TeamArgument extends Argument<ScorePlayerTeam> {
    public TeamArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, net.minecraft.command.arguments.TeamArgument.team());
    }

    @Override
    public ScorePlayerTeam parse(CommandContext<CommandSource> ctx) {
        try {
            return net.minecraft.command.arguments.TeamArgument.getTeam(ctx, name);
        } catch (Exception e) {
            return null;
        }
    }
}
