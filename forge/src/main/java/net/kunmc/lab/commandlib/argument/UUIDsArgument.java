package net.kunmc.lab.commandlib.argument;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UUIDsArgument extends Argument<List<UUID>> {
    public UUIDsArgument(String name) {
        this(name, option -> {
        });
    }

    public UUIDsArgument(String name, Consumer<Option<List<UUID>>> options) {
        super(name, GameProfileArgument.gameProfile());

        setSuggestionAction(sb -> {
            String input = sb.getLatestInput();

            Map<UUID, String> uuidToNameMap = new HashMap<>();
            sb.getHandle()
              .getSource()
              .getPlayerNames()
              .stream()
              .map(getPlayerProfileCache()::getGameProfileForUsername)
              .filter(Objects::nonNull)
              .filter(x -> filter() == null || filter().test(Collections.singletonList(x.getId())))
              .filter(x -> {
                  if (input.isEmpty()) {
                      return true;
                  }

                  if (x.getName() != null && StringUtils.containsIgnoreCase(x.getName(), input)) {
                      return true;
                  }
                  return StringUtils.containsIgnoreCase(x.getId()
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
        setOptions(options);
    }

    @Override
    public List<UUID> cast(Object parsedArgument) {
        return ((List<UUID>) parsedArgument);
    }

    @Override
    public List<UUID> parse(CommandContext<CommandSource> ctx) throws CommandSyntaxException, IncorrectArgumentInputException {
        String s = getInputString(ctx, name);

        if (s.startsWith("@")) {
            List<UUID> uuids = ctx.getSource()
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
                throw new IncorrectArgumentInputException(new StringTextComponent(TextFormatting.RED + "no player found"));
            }
            if (s.equals("@r")) {
                Collections.shuffle(uuids, ThreadLocalRandom.current());
                return Collections.singletonList(uuids.stream()
                                                      .findFirst()
                                                      .orElseThrow(() -> new IncorrectArgumentInputException(new StringTextComponent(
                                                              TextFormatting.RED + "no player found"))));
            }

            throw new IncorrectArgumentInputException(new StringTextComponent(TextFormatting.RED + s + " is invalid selector."));
        }

        GameProfile gameProfile = getPlayerProfileCache().getGameProfileForUsername(s);
        if (gameProfile != null) {
            return Collections.singletonList(gameProfile.getId());
        }

        try {
            return Collections.singletonList(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            throw new IncorrectArgumentInputException(new StringTextComponent(TextFormatting.RED + s + " is not found."),
                                                      new StringTextComponent(TextFormatting.RED + s + " is not valid UUID."));
        }
    }

    private static PlayerProfileCache getPlayerProfileCache() {
        return ServerLifecycleHooks.getCurrentServer()
                                   .getPlayerProfileCache();
    }
}
