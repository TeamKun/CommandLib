package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPlayers;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NMSArgumentPlayers_v1_20_5 extends NMSArgumentPlayers {
    public NMSArgumentPlayers_v1_20_5() {
        super(null, "commands.arguments.EntityArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("players"));
    }

    @Override
    protected List<Player> parseImpl(CommandContext<?> ctx, String name) {
        return ((Collection<?>) invokeStaticMethod("getPlayers", ctx, name)).stream()
                                                                            .map(NMSEntity::create)
                                                                            .map(NMSEntity::getBukkitEntity)
                                                                            .map(x -> ((Player) x))
                                                                            .collect(Collectors.toList());
    }
}
