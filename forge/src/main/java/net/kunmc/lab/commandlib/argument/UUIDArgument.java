package net.kunmc.lab.commandlib.argument;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.SuggestionBuilder;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UUIDArgument extends Argument<UUID> {
    private static final List<String> uuidChars = Collections.unmodifiableList(Lists.newArrayList("a",
                                                                                                  "b",
                                                                                                  "c",
                                                                                                  "d",
                                                                                                  "e",
                                                                                                  "f",
                                                                                                  "A",
                                                                                                  "B",
                                                                                                  "C",
                                                                                                  "D",
                                                                                                  "E",
                                                                                                  "F",
                                                                                                  "1",
                                                                                                  "2",
                                                                                                  "3",
                                                                                                  "4",
                                                                                                  "5",
                                                                                                  "6",
                                                                                                  "7",
                                                                                                  "8",
                                                                                                  "9"));

    public UUIDArgument(String name) {
        this(name, option -> {
        });
    }

    public UUIDArgument(String name, Consumer<Option<UUID>> options) {
        super(name, StringArgumentType.string());

        setSuggestionAction(sb -> {
            Map<String, UUID> nameToUUID = sb.getHandle()
                                             .getSource()
                                             .getPlayerNames()
                                             .stream()
                                             .map(getPlayerProfileCache()::getGameProfileForUsername)
                                             .filter(Objects::nonNull)
                                             .filter(x -> Objects.nonNull(x.getName()))
                                             .filter(x -> filter() == null || filter().test(x.getId()))
                                             .filter(x -> sb.getLatestInput()
                                                            .isEmpty() || x.getName()
                                                                           .contains(sb.getLatestInput()))
                                             .collect(Collectors.toMap(GameProfile::getName, GameProfile::getId));
            nameToUUID.forEach((key, value) -> sb.suggest(key, value.toString()));

            if (nameToUUID.isEmpty()) {
                suggestUUID(sb);
            }
        });
        setOptions(options);
    }

    @Override
    public UUID cast(Object parsedArgument) {
        return ((UUID) parsedArgument);
    }

    @Override
    public UUID parse(CommandContext<CommandSource> ctx) throws CommandSyntaxException, IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);

        GameProfile gameProfile = getPlayerProfileCache().getGameProfileForUsername(s);
        if (gameProfile != null) {
            return gameProfile.getId();
        }

        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            throw new IncorrectArgumentInputException(new StringTextComponent(TextFormatting.RED + s + " is not found."),
                                                      new StringTextComponent(TextFormatting.RED + s + " is not valid UUID."));
        }
    }

    private void suggestUUID(SuggestionBuilder sb) {
        String s = sb.getLatestInput();
        if (s.isEmpty()) {
            return;
        }
        if (!isValidInput(s)) {
            return;
        }

        if (s.length() == 8 || s.length() == 13 || s.length() == 18 || s.length() == 23) {
            sb.suggest(s + "-");
            return;
        }

        uuidChars.stream()
                 .map(x -> s + x)
                 .forEach(sb::suggest);
    }

    private boolean isValidInput(String s) {
        if (s.length() > 36) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            String c = String.valueOf(s.charAt(i));

            if (i == 8 || i == 13 || i == 18 || i == 23) {
                if (!c.equals("-")) {
                    return false;
                }
                continue;
            }

            if (!uuidChars.contains(c)) {
                return false;
            }
        }

        return true;
    }

    private static PlayerProfileCache getPlayerProfileCache() {
        return ServerLifecycleHooks.getCurrentServer()
                                   .getPlayerProfileCache();
    }
}
