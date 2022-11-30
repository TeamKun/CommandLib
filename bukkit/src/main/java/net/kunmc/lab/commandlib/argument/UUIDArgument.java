package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.TextColorUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
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

    public UUIDArgument(String name, Consumer<Option<UUID>> options) {
        super(name, StringArgumentType.string());

        setSuggestionAction(sb -> {
            Map<UUID, String> uuidToNameMap = new HashMap<>();
            Arrays.stream(Bukkit.getOfflinePlayers())
                  .filter(x -> filter() == null || filter().test(x.getUniqueId()))
                  .filter(x -> {
                      String input = sb.getLatestInput();
                      if (input.isEmpty()) {
                          return true;
                      }

                      if (x.getName() != null && x.getName()
                                                  .contains(input)) {
                          return true;
                      }
                      return x.getUniqueId()
                              .toString()
                              .contains(input);
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
        setOptions(options);
    }

    @Override
    public UUID cast(Object parsedArgument) {
        return ((UUID) parsedArgument);
    }

    @Override
    public UUID parse(CommandContext<CommandListenerWrapper> ctx) throws CommandSyntaxException, IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);

        OfflinePlayer p = Bukkit.getOfflinePlayerIfCached(s);
        if (p != null) {
            return p.getUniqueId();
        }

        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            throw new IncorrectArgumentInputException(Component.text(s + " is not found.")
                                                               .color(TextColorUtil.RED),
                                                      Component.text(s + " is not valid UUID.")
                                                               .color(TextColorUtil.RED));

        }
    }
}
