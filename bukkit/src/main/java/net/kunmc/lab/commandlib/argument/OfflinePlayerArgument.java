package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import org.apache.commons.lang3.StringUtils;
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
                  .filter(x -> filter() == null || filter().test(x))
                  .map(OfflinePlayer::getName)
                  .filter(Objects::nonNull)
                  .filter(x -> sb.getLatestInput()
                                 .isEmpty() || StringUtils.containsIgnoreCase(x, sb.getLatestInput()))
                  .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    public OfflinePlayer cast(Object parsedArgument) {
        return ((OfflinePlayer) parsedArgument);
    }

    @Override
    public OfflinePlayer parse(CommandContext ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name);
        return Arrays.stream(Bukkit.getOfflinePlayers())
                     .filter(x -> x.getName() != null && x.getName()
                                                          .equalsIgnoreCase(s))
                     .findFirst()
                     .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
