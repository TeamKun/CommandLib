package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

public class TeamArgument extends Argument<Team, TeamArgument> {
    public TeamArgument(String name) {
        super(name, StringArgumentType.word());

        addSuggestionAction(sb -> {
            String input = sb.getLatestInput();
            Bukkit.getScoreboardManager()
                  .getMainScoreboard()
                  .getTeams()
                  .stream()
                  .filter(filter(sb.getContext()))
                  .map(Team::getName)
                  .filter(x -> input.isEmpty() || StringUtil.containsIgnoreCase(x, input))
                  .forEach(sb::suggest);
        });
    }

    @Override
    public Team cast(Object parsedArgument) {
        return (Team) parsedArgument;
    }

    @Override
    protected Team parseImpl(CommandContext ctx) throws ArgumentParseException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());
        Team team = Bukkit.getScoreboardManager()
                          .getMainScoreboard()
                          .getTeam(s);
        if (team == null) {
            throw ArgumentParseException.ofIncorrectInput(this.name(), ctx, s);
        }
        return team;
    }
}
