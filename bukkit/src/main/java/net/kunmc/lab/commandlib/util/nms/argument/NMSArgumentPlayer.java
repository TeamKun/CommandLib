package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Player;

public class NMSArgumentPlayer extends NMSArgument<Player> {
    public NMSArgumentPlayer() {
        super("ArgumentEntity", "commands.arguments.ArgumentEntity", "commands.arguments.EntityArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeMethod("c", "player"));
    }

    @Override
    protected Player parseImpl(CommandContext<?> ctx, String name) {
        return ((Player) new NMSEntity(invokeMethod("e", "getPlayer", ctx, name)).getBukkitEntity());
    }
}
