package net.kunmc.lab.commandlib.argument;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentProfile;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OfflinePlayersArgument extends Argument<List<OfflinePlayer>> {
    public OfflinePlayersArgument(String name) {
        this(name, option -> {
        });
    }

    public OfflinePlayersArgument(String name, Consumer<Option<List<OfflinePlayer>, CommandContext>> options) {
        super(name, new NMSArgumentProfile().argument());

        setSuggestionAction(sb -> {
            String input = sb.getLatestInput();

            Arrays.stream(Bukkit.getOfflinePlayers())
                  .filter(x -> test(Collections.singletonList(x), true))
                  .filter(x -> {
                      if (input.isEmpty()) {
                          return true;
                      }

                      return x.getName() != null && StringUtils.containsIgnoreCase(x.getName(), input);
                  })
                  .map(OfflinePlayer::getName)
                  .filter(Objects::nonNull)
                  .forEach(sb::suggest);

            Lists.newArrayList("@a", "@r")
                 .stream()
                 .filter(x -> input.isEmpty() || x.startsWith(input))
                 .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    public List<OfflinePlayer> cast(Object parsedArgument) {
        return ((List<OfflinePlayer>) parsedArgument);
    }

    @Override
    protected List<OfflinePlayer> parseImpl(CommandContext ctx) throws CommandSyntaxException, IncorrectArgumentInputException {
        String s = ctx.getInput(name);

        if (s.startsWith("@")) {
            List<OfflinePlayer> players = Arrays.stream(Bukkit.getOfflinePlayers())
                                                .collect(Collectors.toList());
            if (s.equals("@a")) {
                if (!players.isEmpty()) {
                    return players;
                }
                throw new IncorrectArgumentInputException(x -> x.sendFailure("no player found."));
            }
            if (s.equals("@r")) {
                Collections.shuffle(players, ThreadLocalRandom.current());
                return Collections.singletonList(players.stream()
                                                        .findFirst()
                                                        .orElseThrow(() -> new IncorrectArgumentInputException(x -> x.sendFailure(
                                                                "no player found."))));
            }

            throw new IncorrectArgumentInputException(x -> x.sendFailure(s + " is invalid selector."));
        }

        OfflinePlayer p = Bukkit.getOfflinePlayerIfCached(s);
        if (p == null) {
            throw new IncorrectArgumentInputException(this, ctx, s);
        }

        return Collections.singletonList(p);
    }
}
