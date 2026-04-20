package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEntity;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Entity;

public class NMSArgumentEntity_v1_20_5 extends NMSArgumentEntity {
    public NMSArgumentEntity_v1_20_5() {
        super(null, "commands.arguments.EntityArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("entity"));
    }

    @Override
    protected Entity parseImpl(CommandContext<?> ctx, String name) {
        return NMSEntity.create(invokeStaticMethod("getEntity", ctx, name))
                        .getBukkitEntity();
    }
}
