package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import java.util.function.Consumer;

public class TeamArgument extends Argument<Team> {
    public TeamArgument(String name) {
        this(name, option -> {
        });
    }

    public TeamArgument(String name, Consumer<Option<Team, CommandContext>> options) {
        super(name, new NMSArgumentScoreboardTeam().argument());
        setOptions(options);
    }

    @Override
    public Team cast(Object parsedArgument) {
        return ((Team) parsedArgument);
    }

    @Override
    protected Team parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return Bukkit.getScoreboardManager()
                     .getMainScoreboard()
                     .getTeam(new NMSArgumentScoreboardTeam().parse(ctx.getHandle(), name)
                                                             .getName());
    }
}
