package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDArgument extends Argument<UUID, UUIDArgument> {
    public UUIDArgument(String name) {
        super(name, StringArgumentType.word());

        suggestionAction(sb -> {
            Map<UUID, String> uuidToNameMap = new HashMap<>();
            System.out.println(Arrays.toString(Bukkit.getOfflinePlayers()));
            Arrays.stream(Bukkit.getOfflinePlayers())
                  .filter(x -> filter(sb.getContext()).test(x.getUniqueId()))
                  .filter(x -> {
                      String input = sb.getLatestInput();
                      System.out.println(x.getName() + " " + input);
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
            System.out.println(uuidToNameMap);
            uuidToNameMap.forEach((k, v) -> {
                System.out.println(k);
                if (v == null) {
                    sb.suggest(k.toString());
                } else {
                    sb.suggest(v, k.toString());
                }
            });
        });
        displayDefaultSuggestions(false);
    }

    @Override
    public UUID cast(Object parsedArgument) {
        return (UUID) parsedArgument;
    }

    @Override
    protected UUID parseImpl(CommandContext ctx) throws ArgumentParseException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());

        OfflinePlayer p = getOfflinePlayerByName(s);
        if (p != null) {
            return p.getUniqueId();
        }

        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            throw new ArgumentParseException(x -> x.sendFailure(s + " is not found or not valid UUID"));
        }
    }

    private static OfflinePlayer getOfflinePlayerByName(String name) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                     .filter(x -> name.equals(x.getName()))
                     .findFirst()
                     .orElse(null);
    }
}
