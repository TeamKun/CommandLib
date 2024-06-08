package net.kunmc.lab.commandlib.util.nms.argument.v1_17_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPlayer;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Player;

public class NMSArgumentPlayer_v1_17_0 extends NMSArgumentPlayer {
    public NMSArgumentPlayer_v1_17_0() {
        super(null, "commands.arguments.ArgumentEntity");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("c"));
    }

    @Override
    protected Player parseImpl(CommandContext<?> ctx, String name) {
        return ((Player) NMSEntity.create(invokeStaticMethod("e", ctx, name))
                                  .getBukkitEntity());
    }
}
