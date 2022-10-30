package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.util.function.Consumer;

public class TeamArgument extends Argument<ScorePlayerTeam> {
    public TeamArgument(String name) {
        this(name, option -> {
        });
    }

    public TeamArgument(String name, Consumer<Option<ScorePlayerTeam>> options) {
        super(name, net.minecraft.command.arguments.TeamArgument.team());
        setOptions(options);
    }

    @Override
    public ScorePlayerTeam cast(Object parsedArgument) {
        return ((ScorePlayerTeam) parsedArgument);
    }

    @Override
    public ScorePlayerTeam parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return net.minecraft.command.arguments.TeamArgument.getTeam(ctx, name);
    }
}
