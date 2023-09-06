package net.kunmc.lab.commandlib.argument;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.StringUtil;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
            sb.getContext()
              .getHandle()
              .getSource()
              .getPlayerNames()
              .stream()
              .map(getPlayerProfileCache()::getGameProfileForUsername)
              .filter(Objects::nonNull)
              .filter(x -> filter().test(x.getId()))
              .filter(x -> {
                  String input = sb.getLatestInput();
                  if (input.isEmpty()) {
                      return true;
                  }

                  if (x.getName() != null && StringUtil.containsIgnoreCase(x.getName(), input)) {
                      return true;
                  }
                  return StringUtil.containsIgnoreCase(x.getId()
                                                        .toString(), input);
              })
              .forEach(x -> uuidToNameMap.put(x.getId(), x.getName()));

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

        GameProfile gameProfile = getPlayerProfileCache().getGameProfileForUsername(s);
        if (gameProfile != null) {
            return gameProfile.getId();
        }

        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            throw new IncorrectArgumentInputException(x -> x.sendFailure(s + " is not found or not valid UUID"));
        }
    }

    private static PlayerProfileCache getPlayerProfileCache() {
        return ServerLifecycleHooks.getCurrentServer()
                                   .getPlayerProfileCache();
    }
}
