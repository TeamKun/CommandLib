package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.util.function.Consumer;

public class TeamArgument extends Argument<ScorePlayerTeam> {
    public TeamArgument(String name) {
        this(name, option -> {
        });
    }

    public TeamArgument(String name, Consumer<Option<ScorePlayerTeam, CommandContext>> options) {
        super(name, net.minecraft.command.arguments.TeamArgument.team());
        setOptions(options);
    }

    @Override
    public ScorePlayerTeam cast(Object parsedArgument) {
        return ((ScorePlayerTeam) parsedArgument);
    }

    @Override
    protected ScorePlayerTeam parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return net.minecraft.command.arguments.TeamArgument.getTeam(ctx.getHandle(), name());
    }
}
