package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NMSArgumentPlayers extends NMSArgument<List<Player>> {
    public NMSArgumentPlayers() {
        super("ArgumentEntity", "commands.arguments.ArgumentEntity", "commands.arguments.EntityArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeMethod("d", "players"));
    }

    @Override
    protected List<Player> parseImpl(CommandContext<?> ctx, String name) {
        return ((Collection<?>) invokeMethod("f", "getPlayers", ctx, name)).stream()
                                                                           .map(NMSEntity::new)
                                                                           .map(NMSEntity::getBukkitEntity)
                                                                           .map(x -> ((Player) x))
                                                                           .collect(Collectors.toList());
    }
}
