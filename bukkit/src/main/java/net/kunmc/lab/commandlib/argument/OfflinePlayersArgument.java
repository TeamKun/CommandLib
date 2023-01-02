package net.kunmc.lab.commandlib.argument;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.TextColorUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_16_R3.ArgumentProfile;
import org.apache.commons.lang.StringUtils;
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

    public OfflinePlayersArgument(String name, Consumer<Option<List<OfflinePlayer>>> options) {
        super(name, ArgumentProfile.a());

        setSuggestionAction(sb -> {
            String input = sb.getLatestInput();

            Arrays.stream(Bukkit.getOfflinePlayers())
                  .filter(x -> filter() == null || filter().test(Collections.singletonList(x)))
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
    public List<OfflinePlayer> parse(CommandContext ctx) throws CommandSyntaxException, IncorrectArgumentInputException {
        String s = ctx.getInput(name);

        if (s.startsWith("@")) {
            List<OfflinePlayer> players = Arrays.stream(Bukkit.getOfflinePlayers())
                                                .collect(Collectors.toList());
            if (s.equals("@a")) {
                if (!players.isEmpty()) {
                    return players;
                }
                throw new IncorrectArgumentInputException(Component.text("no player found")
                                                                   .color(TextColorUtil.RED));
            }
            if (s.equals("@r")) {
                Collections.shuffle(players, ThreadLocalRandom.current());
                return Collections.singletonList(players.stream()
                                                        .findFirst()
                                                        .orElseThrow(() -> new IncorrectArgumentInputException(Component.text(
                                                                                                                                "no player found")
                                                                                                                        .color(TextColorUtil.RED))));
            }

            throw new IncorrectArgumentInputException(Component.text(s + " is invalid selector.")
                                                               .color(TextColorUtil.RED));
        }

        OfflinePlayer p = Bukkit.getOfflinePlayerIfCached(s);
        if (p == null) {
            throw new IncorrectArgumentInputException(this, ctx, s);
        }

        return Collections.singletonList(p);
    }
}
