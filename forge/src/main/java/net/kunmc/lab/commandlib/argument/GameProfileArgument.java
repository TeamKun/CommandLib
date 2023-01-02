package net.kunmc.lab.commandlib.argument;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
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

    public GameProfileArgument(String name, Consumer<Option<GameProfile>> options) {
        super(name, StringArgumentType.string());

        setSuggestionAction(sb -> {
            sb.getHandle()
              .getSource()
              .getPlayerNames()
              .stream()
              .map(getPlayerProfileCache()::getGameProfileForUsername)
              .filter(Objects::nonNull)
              .filter(x -> Objects.nonNull(x.getName()))
              .filter(x -> filter() == null || filter().test(x))
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
    public GameProfile parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return ctx.getSource()
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
