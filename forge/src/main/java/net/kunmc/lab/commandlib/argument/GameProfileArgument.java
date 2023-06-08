package net.kunmc.lab.commandlib.argument;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Consumer;

public class GameProfileArgument extends Argument<GameProfile> {
    public GameProfileArgument(String name) {
        this(name, option -> {
        });
    }

    public GameProfileArgument(String name, Consumer<Option<GameProfile, CommandContext>> options) {
        super(name, StringArgumentType.string());

        setSuggestionAction(sb -> {
            sb.getContext()
              .getHandle()
              .getSource()
              .getPlayerNames()
              .stream()
              .map(getPlayerProfileCache()::getGameProfileForUsername)
              .filter(Objects::nonNull)
              .filter(x -> Objects.nonNull(x.getName()))
              .filter(x -> test(x, true))
              .map(GameProfile::getName)
              .filter(x -> sb.getLatestInput()
                             .isEmpty() || StringUtils.containsIgnoreCase(x, sb.getLatestInput()))
              .forEach(sb::suggest);
        });
        setOptions(options);
    }

    @Override
    public GameProfile cast(Object parsedArgument) {
        return ((GameProfile) parsedArgument);
    }

    @Override
    protected GameProfile parseImpl(CommandContext ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());
        return ctx.getHandle()
                  .getSource()
                  .getPlayerNames()
                  .stream()
                  .map(getPlayerProfileCache()::getGameProfileForUsername)
                  .filter(Objects::nonNull)
                  .filter(x -> x.getName()
                                .equalsIgnoreCase(s))
                  .findFirst()
                  .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }

    private static PlayerProfileCache getPlayerProfileCache() {
        return ServerLifecycleHooks.getCurrentServer()
                                   .getPlayerProfileCache();
    }
}
