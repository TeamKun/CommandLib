package net.kunmc.lab.commandlib.argument;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.StringUtil;
import net.kunmc.lab.commandlib.util.bukkit.BukkitUtil;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentProfile;
import net.md_5.bungee.api.chat.TextComponent;
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
        super(name,
              NMSArgumentProfile.create()
                                .argument());

        suggestionAction(sb -> {
            String input = sb.getLatestInput();

            Map<UUID, String> uuidToNameMap = new HashMap<>();
            Arrays.stream(Bukkit.getOfflinePlayers())
                  .filter(x -> filter(sb.getContext()).test(Collections.singletonList(x.getUniqueId())))
                  .filter(x -> {
                      if (input.isEmpty()) {
                          return true;
                      }

                      if (x.getName() != null && StringUtil.containsIgnoreCase(x.getName(), input)) {
                          return true;
                      }
                      return StringUtil.containsIgnoreCase(x.getUniqueId()
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
        displayDefaultSuggestions(false);
        applyOptions(options);
    }

    @Override
    public List<UUID> cast(Object parsedArgument) {
        return ((List<UUID>) parsedArgument);
    }

    @Override
    protected List<UUID> parseImpl(CommandContext ctx) throws CommandSyntaxException, ArgumentParseException {
        String s = ctx.getInput(name());

        if (s.startsWith("@")) {
            List<UUID> uuids = Arrays.stream(Bukkit.getOfflinePlayers())
                                     .map(OfflinePlayer::getUniqueId)
                                     .collect(Collectors.toList());
            if (s.equals("@a")) {
                if (!uuids.isEmpty()) {
                    return uuids;
                }
                throw new ArgumentParseException(x -> ((CommandContext) x).sendFailure(new TextComponent(
                        "no player found")));
            }
            if (s.equals("@r")) {
                Collections.shuffle(uuids, ThreadLocalRandom.current());
                return Collections.singletonList(uuids.stream()
                                                      .findFirst()
                                                      .orElseThrow(() -> new ArgumentParseException(x -> ((CommandContext) x).sendFailure(
                                                              new TextComponent("no player found")))));
            }

            throw new ArgumentParseException(x -> ((CommandContext) x).sendFailure(new TextComponent(s + " is invalid selector")));
        }

        OfflinePlayer p = BukkitUtil.getOfflinePlayerIfEverPlayed(s);
        if (p != null) {
            return Collections.singletonList(p.getUniqueId());
        }

        try {
            return Collections.singletonList(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            throw new ArgumentParseException(x -> {
                ((CommandContext) x).sendFailure(new TextComponent(s + " is not found or not valid UUID"));
            });
        }
    }
}
