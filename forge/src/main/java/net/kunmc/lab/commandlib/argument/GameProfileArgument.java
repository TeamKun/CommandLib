package net.kunmc.lab.commandlib.argument;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Objects;
import java.util.function.Predicate;

public class GameProfileArgument extends Argument<GameProfile> {
    private final Predicate<? super GameProfile> filter;

    public GameProfileArgument(String name, Predicate<? super GameProfile> filter, ContextAction contextAction) {
        super(name, sb -> {
            sb.getHandle().getSource().getPlayerNames().stream()
                    .map(getPlayerProfileCache()::getGameProfileForUsername)
                    .filter(Objects::nonNull)
                    .filter(x -> Objects.nonNull(x.getName()))
                    .filter(x -> filter == null || filter.test(x))
                    .map(GameProfile::getName)
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.filter = filter;
    }

    @Override
    public GameProfile parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return ctx.getSource().getPlayerNames().stream()
                .map(getPlayerProfileCache()::getGameProfileForUsername)
                .filter(Objects::nonNull)
                .filter(x -> filter == null || filter.test(x))
                .filter(x -> x.getName().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }

    private static PlayerProfileCache getPlayerProfileCache() {
        return ServerLifecycleHooks.getCurrentServer().getPlayerProfileCache();
    }
}
