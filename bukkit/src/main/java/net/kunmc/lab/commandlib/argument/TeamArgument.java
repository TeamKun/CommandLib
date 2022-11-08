package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentScoreboardTeam;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import java.util.function.Consumer;

public class TeamArgument extends Argument<Team> {
    public TeamArgument(String name) {
        this(name, option -> {
        });
    }

    public TeamArgument(String name, Consumer<Option<Team>> options) {
        super(name, ArgumentScoreboardTeam.a());
        setOptions(options);
    }

    @Override
    public Team cast(Object parsedArgument) {
        return ((Team) parsedArgument);
    }

    @Override
    public Team parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return Bukkit.getScoreboardManager()
                     .getMainScoreboard()
                     .getTeam(ArgumentScoreboardTeam.a(ctx, name)
                                                    .getName());
    }
}
