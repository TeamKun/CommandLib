package net.kunmc.lab.commandlib.argument;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.TextColorUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_16_R3.ArgumentProfile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UUIDsArgument extends Argument<List<UUID>> {
    public UUIDsArgument(String name) {
        this(name, option -> {
        });
    }

    public UUIDsArgument(String name, Consumer<Option<List<UUID>, CommandContext>> options) {
        super(name, ArgumentProfile.a());

        setSuggestionAction(sb -> {
            String input = sb.getLatestInput();

            Map<UUID, String> uuidToNameMap = new HashMap<>();
            Arrays.stream(Bukkit.getOfflinePlayers())
                  .filter(x -> filter() == null || filter().test(Collections.singletonList(x.getUniqueId())))
                  .filter(x -> {
                      if (input.isEmpty()) {
                          return true;
                      }

                      if (x.getName() != null && StringUtils.containsIgnoreCase(x.getName(), input)) {
                          return true;
                      }
                      return StringUtils.containsIgnoreCase(x.getUniqueId()
                                                             .toString(), input);
                  })
                  .forEach(x -> uuidToNameMap.put(x.getUniqueId(), x.getName()));
            uuidToNameMap.forEach((k, v) -> {
                if (v == null) {
                    sb.suggest(k.toString());
                } else {
                    sb.suggest(v, k.toString());
                }
            });

            Lists.newArrayList("@a", "@r")
                 .stream()
                 .filter(x -> input.isEmpty() || x.startsWith(input))
                 .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    public List<UUID> cast(Object parsedArgument) {
        return ((List<UUID>) parsedArgument);
    }

    @Override
    public List<UUID> parse(CommandContext ctx) throws CommandSyntaxException, IncorrectArgumentInputException {
        String s = ctx.getInput(name);

        if (s.startsWith("@")) {
            List<UUID> uuids = Arrays.stream(Bukkit.getOfflinePlayers())
                                     .map(OfflinePlayer::getUniqueId)
                                     .collect(Collectors.toList());
            if (s.equals("@a")) {
                if (!uuids.isEmpty()) {
                    return uuids;
                }
                throw new IncorrectArgumentInputException(x -> ((CommandContext) x).sendMessage(Component.text(
                                                                                                                 "no player found")
                                                                                                         .color(TextColorUtil.RED)));
            }
            if (s.equals("@r")) {
                Collections.shuffle(uuids, ThreadLocalRandom.current());
                return Collections.singletonList(uuids.stream()
                                                      .findFirst()
                                                      .orElseThrow(() -> new IncorrectArgumentInputException(x -> ((CommandContext) x).sendMessage(
                                                              Component.text("no player found")
                                                                       .color(TextColorUtil.RED)))));
            }

            throw new IncorrectArgumentInputException(x -> ((CommandContext) x).sendMessage(Component.text(s + " is invalid selector.")
                                                                                                     .color(TextColorUtil.RED)));
        }

        OfflinePlayer p = Bukkit.getOfflinePlayerIfCached(s);
        if (p != null) {
            return Collections.singletonList(p.getUniqueId());
        }

        try {
            return Collections.singletonList(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            throw new IncorrectArgumentInputException(x -> {
                ((CommandContext) x).sendMessage(Component.text(s + " is not found or not valid UUID")
                                                          .color(TextColorUtil.RED));
            });
        }
    }
}
