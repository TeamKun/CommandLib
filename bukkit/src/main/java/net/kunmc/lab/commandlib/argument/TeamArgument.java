package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.v1_16_R3.ArgumentScoreboardTeam;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class TeamArgument extends Argument<Team> {
    public TeamArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentScoreboardTeam.a());
    }

    @Override
    public Team parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(ArgumentScoreboardTeam.a(ctx, name).getName());
        } catch (CommandSyntaxException e) {
            throw new IncorrectArgumentInputException(Component.translatable("team.notFound", Component.text(ctx.getArgument(name, String.class)))
                    .color(TextColor.color(ChatColor.RED.asBungee().getColor().getRGB())));
        }
    }
}
