package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.function.Consumer;

public class WorldArgument extends Argument<World> {
    public WorldArgument(String name) {
        this(name, option -> {
        });
    }

    public WorldArgument(String name, Consumer<Option<World, CommandContext>> options) {
        super(name, StringArgumentType.word());

        suggestionAction(sb -> {
            String input = sb.getLatestInput();
            Bukkit.getWorlds()
                  .stream()
                  .filter(x -> filter(sb.getContext()).test(x))
                  .map(World::getName)
                  .filter(x -> input.isEmpty() || x.contains(input))
                  .forEach(sb::suggest);
        });
        applyOptions(options);
    }

    @Override
    public World cast(Object parsedArgument) {
        return (World) parsedArgument;
    }

    @Override
    protected World parseImpl(CommandContext ctx) throws CommandSyntaxException, ArgumentParseException {
        String name = StringArgumentType.getString(ctx.getHandle(), name());
        World world = Bukkit.getWorld(name);
        if (world == null) {
            throw ArgumentParseException.ofIncorrectInput(name(), ctx, name);
        }
        return world;
    }
}
