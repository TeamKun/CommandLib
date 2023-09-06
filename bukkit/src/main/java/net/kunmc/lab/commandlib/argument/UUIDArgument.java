package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class UUIDArgument extends Argument<UUID> {
    public UUIDArgument(String name) {
        this(name, option -> {
        });
    }

    public UUIDArgument(String name, Consumer<Option<UUID, CommandContext>> options) {
        super(name, StringArgumentType.string());

        setSuggestionAction(sb -> {
            Map<UUID, String> uuidToNameMap = new HashMap<>();
            Arrays.stream(Bukkit.getOfflinePlayers())
                  .filter(x -> filter().test(x.getUniqueId()))
                  .filter(x -> {
                      String input = sb.getLatestInput();
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
        });
        setDisplayDefaultSuggestions(false);
        applyOptions(options);
    }

    @Override
    public UUID cast(Object parsedArgument) {
        return ((UUID) parsedArgument);
    }

    @Override
    protected UUID parseImpl(CommandContext ctx) throws CommandSyntaxException, IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());

        OfflinePlayer p = Bukkit.getOfflinePlayerIfCached(s);
        if (p != null) {
            return p.getUniqueId();
        }

        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            throw new IncorrectArgumentInputException(x -> {
                x.sendFailure(s + " is not found or not valid UUID");
            });
        }
    }
}
