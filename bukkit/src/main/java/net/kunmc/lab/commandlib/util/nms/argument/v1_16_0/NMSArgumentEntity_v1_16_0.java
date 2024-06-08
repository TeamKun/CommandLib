package net.kunmc.lab.commandlib.util.nms.argument.v1_16_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEntity;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Entity;

public class NMSArgumentEntity_v1_16_0 extends NMSArgumentEntity {
    public NMSArgumentEntity_v1_16_0() {
        super(null, "ArgumentEntity");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("a"));
    }

    @Override
    protected Entity parseImpl(CommandContext<?> ctx, String name) {
        return NMSEntity.create(invokeStaticMethod("a", ctx, name))
                        .getBukkitEntity();
    }
}
