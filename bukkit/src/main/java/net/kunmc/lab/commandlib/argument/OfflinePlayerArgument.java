package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class OfflinePlayerArgument extends Argument<OfflinePlayer> {
    private final Predicate<? super OfflinePlayer> filter;

    public OfflinePlayerArgument(String name, Predicate<? super OfflinePlayer> filter, ContextAction contextAction) {
        super(name, sb -> {
            Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(x -> filter == null || filter.test(x))
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.filter = filter;
    }

    @Override
    public OfflinePlayer parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(x -> filter == null || filter.test(x))
                .filter(x -> x.getName() != null && x.getName().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
