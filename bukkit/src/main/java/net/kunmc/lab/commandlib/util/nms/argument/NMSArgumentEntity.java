package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Entity;

public class NMSArgumentEntity extends NMSArgument<Entity> {
    public NMSArgumentEntity() {
        super("ArgumentEntity", "commands.arguments.ArgumentEntity", "commands.arguments.EntityArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeMethod("a", "entity"));
    }

    @Override
    protected Entity parseImpl(CommandContext<?> ctx, String name) {
        return new NMSEntity(invokeMethod("a", "getEntity", ctx, name)).getBukkitEntity();
    }
}
