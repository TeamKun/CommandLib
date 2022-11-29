package net.kunmc.lab.commandlib.argument;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.SuggestionBuilder;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
            Map<String, UUID> nameToUUID = Arrays.stream(Bukkit.getOfflinePlayers())
                                                 .filter(x -> x.getName() != null)
                                                 .filter(x -> filter() == null || filter().test(x.getUniqueId()))
                                                 .filter(x -> sb.getLatestInput()
                                                                .isEmpty() || x.getName()
                                                                               .contains(sb.getLatestInput()))
                                                 .collect(Collectors.toMap(OfflinePlayer::getName,
                                                                           OfflinePlayer::getUniqueId));
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
                                                               .color(TextColor.color(ChatColor.RED.getColor()
                                                                                                   .getRGB())),
                                                      Component.text(s + " is not valid UUID.")
                                                               .color(TextColor.color(ChatColor.RED.getColor()
                                                                                                   .getRGB())));
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
}
