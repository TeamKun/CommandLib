package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.ArgumentScoreboardTeam;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

public class TeamArgument extends Argument<Team> {
    public TeamArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentScoreboardTeam.a());
    }

    @Override
    public Team parse(CommandContext<CommandListenerWrapper> ctx) {
        try {
            return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(ArgumentScoreboardTeam.a(ctx, name).getName());
        } catch (CommandSyntaxException e) {
            return null;
        }
    }
}
