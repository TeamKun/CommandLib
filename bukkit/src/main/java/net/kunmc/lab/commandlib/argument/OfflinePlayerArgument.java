package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class OfflinePlayerArgument extends Argument<OfflinePlayer> {
    public OfflinePlayerArgument(String name) {
        this(name, option -> {
        });
    }

    public OfflinePlayerArgument(String name, Consumer<Option<OfflinePlayer>> options) {
        super(name, StringArgumentType.string());
        setSuggestionAction(sb -> {
            Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(x -> filter() == null || filter().test(x))
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    protected OfflinePlayer cast(Object parsedArgument) {
        return ((OfflinePlayer) parsedArgument);
    }

    @Override
    public OfflinePlayer parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(x -> x.getName() != null && x.getName().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
