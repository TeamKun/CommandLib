package net.kunmc.lab.commandlib.util.nms.argument.v1_16_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEntities;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NMSArgumentEntities_v1_16_0 extends NMSArgumentEntities {
    public NMSArgumentEntities_v1_16_0() {
        super(null, "ArgumentEntity");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("multipleEntities"));
    }

    @Override
    protected List<Entity> parseImpl(CommandContext<?> ctx, String name) {
        return ((Collection<?>) invokeStaticMethod("b", ctx, name)).stream()
                                                                   .map(NMSEntity::create)
                                                                   .map(NMSEntity::getBukkitEntity)
                                                                   .collect(Collectors.toList());
    }
}
