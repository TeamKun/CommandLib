package net.kunmc.lab.commandlib.argument;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.StringUtil;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

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
        super(name, GameProfileArgument.gameProfile());

        setSuggestionAction(sb -> {
            String input = sb.getLatestInput();

            Map<UUID, String> uuidToNameMap = new HashMap<>();
            sb.getContext()
              .getHandle()
              .getSource()
              .getPlayerNames()
              .stream()
              .map(getPlayerProfileCache()::getGameProfileForUsername)
              .filter(Objects::nonNull)
              .filter(x -> filter().apply(Collections.singletonList(x.getId()), sb.getContext()))
              .filter(x -> {
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

            Lists.newArrayList("@a", "@r")
                 .stream()
                 .filter(x -> input.isEmpty() || x.startsWith(input))
                 .forEach(sb::suggest);
        });
        setDisplayDefaultSuggestions(false);
        applyOptions(options);
    }

    @Override
    public List<UUID> cast(Object parsedArgument) {
        return ((List<UUID>) parsedArgument);
    }

    @Override
    protected List<UUID> parseImpl(CommandContext ctx) throws CommandSyntaxException, IncorrectArgumentInputException {
        String s = ctx.getInput(name());

        if (s.startsWith("@")) {
            List<UUID> uuids = ctx.getHandle()
                                  .getSource()
                                  .getPlayerNames()
                                  .stream()
                                  .map(getPlayerProfileCache()::getGameProfileForUsername)
                                  .filter(Objects::nonNull)
                                  .map(GameProfile::getId)
                                  .collect(Collectors.toList());
            if (s.equals("@a")) {
                if (!uuids.isEmpty()) {
                    return uuids;
                }
                throw new IncorrectArgumentInputException(x -> x.sendFailure("no player found"));
            }
            if (s.equals("@r")) {
                Collections.shuffle(uuids, ThreadLocalRandom.current());
                return Collections.singletonList(uuids.stream()
                                                      .findFirst()
                                                      .orElseThrow(() -> new IncorrectArgumentInputException(x -> x.sendFailure(
                                                              "no player found"))));
            }

            throw new IncorrectArgumentInputException(x -> x.sendFailure(s + " is invalid selector"));
        }

        GameProfile gameProfile = getPlayerProfileCache().getGameProfileForUsername(s);
        if (gameProfile != null) {
            return Collections.singletonList(gameProfile.getId());
        }

        try {
            return Collections.singletonList(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            throw new IncorrectArgumentInputException(x -> x.sendFailure(s + " is not found or not valid UUID"));
        }
    }

    private static PlayerProfileCache getPlayerProfileCache() {
        return ServerLifecycleHooks.getCurrentServer()
                                   .getPlayerProfileCache();
    }
}
