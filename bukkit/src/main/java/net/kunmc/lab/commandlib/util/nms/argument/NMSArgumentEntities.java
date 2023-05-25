package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NMSArgumentEntities extends NMSArgument<List<Entity>> {
    public NMSArgumentEntities() {
        super("ArgumentEntity", "commands.arguments.ArgumentEntity", "commands.arguments.EntityArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeMethod("multipleEntities", "b", "entities"));
    }

    @Override
    protected List<Entity> parseImpl(CommandContext<?> ctx, String name) {
        return ((Collection<?>) invokeMethod("b", "getEntities", ctx, name)).stream()
                                                                            .map(NMSEntity::new)
                                                                            .map(NMSEntity::getBukkitEntity)
                                                                            .collect(Collectors.toList());
    }
}
