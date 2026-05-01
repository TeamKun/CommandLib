package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.GameMode;

@SuppressWarnings("UnstableApiUsage")
public class GameModeArgument extends Argument<GameMode, GameModeArgument> {
    public GameModeArgument(String name) {
        super(name, ArgumentTypes.gameMode());
    }

    @Override
    public GameMode cast(Object parsedArgument) {
        return (GameMode) parsedArgument;
    }

    @Override
    protected GameMode parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return ctx.getHandle()
                  .getArgument(name(), GameMode.class);
    }
}
