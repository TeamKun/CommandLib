package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPlayer;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Player;

public class NMSArgumentPlayer_v1_20_5 extends NMSArgumentPlayer {
    public NMSArgumentPlayer_v1_20_5() {
        super(null, "commands.arguments.EntityArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("player"));
    }

    @Override
    protected Player parseImpl(CommandContext<?> ctx, String name) {
        return ((Player) NMSEntity.create(invokeStaticMethod("getPlayer", ctx, name))
                                  .getBukkitEntity());
    }
}
