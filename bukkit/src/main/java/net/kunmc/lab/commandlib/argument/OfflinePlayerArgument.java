package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class OfflinePlayerArgument extends Argument<OfflinePlayer> {
    public OfflinePlayerArgument(String name) {
        this(name, option -> {
        });
    }

    public OfflinePlayerArgument(String name, Consumer<Option<OfflinePlayer, CommandContext>> options) {
        super(name, StringArgumentType.string());

        setSuggestionAction(sb -> {
            Arrays.stream(Bukkit.getOfflinePlayers())
                  .filter(x -> filter().apply(x, sb.getContext()))
                  .map(OfflinePlayer::getName)
                  .filter(Objects::nonNull)
                  .filter(x -> sb.getLatestInput()
                                 .isEmpty() || StringUtil.containsIgnoreCase(x, sb.getLatestInput()))
                  .forEach(sb::suggest);
        });
        applyOptions(options);
    }

    @Override
    public OfflinePlayer cast(Object parsedArgument) {
        return ((OfflinePlayer) parsedArgument);
    }

    @Override
    protected OfflinePlayer parseImpl(CommandContext ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());
        return Arrays.stream(Bukkit.getOfflinePlayers())
                     .filter(x -> x.getName() != null && x.getName()
                                                          .equalsIgnoreCase(s))
                     .findFirst()
                     .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
