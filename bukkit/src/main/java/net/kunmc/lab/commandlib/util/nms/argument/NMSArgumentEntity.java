package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.world.NMSEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NMSArgumentEntity extends MinecraftClass {
    public NMSArgumentEntity() {
        super(null, "ArgumentEntity", "commands.arguments.ArgumentEntity", "commands.arguments.EntityArgument");
    }

    public ArgumentType<?> entityArgument() {
        return ((ArgumentType<?>) invokeMethod("a", "entity"));
    }

    public ArgumentType<?> entitiesArgument() {
        return ((ArgumentType<?>) invokeMethod("multipleEntities", "b", "entities"));
    }

    public ArgumentType<?> playerArgument() {
        return ((ArgumentType<?>) invokeMethod("c", "player"));
    }

    public ArgumentType<?> playersArgument() {
        return ((ArgumentType<?>) invokeMethod("d", "players"));
    }

    public Entity getEntity(CommandContext<?> ctx, String name) {
        return new NMSEntity(invokeMethod("a", "getEntity", ctx, name)).getBukkitEntity();
    }

    public List<Entity> getEntities(CommandContext<?> ctx, String name) {
        return ((Collection<?>) invokeMethod("b", "getEntities", ctx, name)).stream()
                                                                            .map(NMSEntity::new)
                                                                            .map(NMSEntity::getBukkitEntity)
                                                                            .collect(Collectors.toList());
    }

    public Player getPlayer(CommandContext<?> ctx, String name) {
        return ((Player) new NMSEntity(invokeMethod("e", "getPlayer", ctx, name)).getBukkitEntity());
    }

    public List<Player> getPlayers(CommandContext<?> ctx, String name) {
        return ((Collection<?>) invokeMethod("f", "getPlayers", ctx, name)).stream()
                                                                           .map(NMSEntity::new)
                                                                           .map(NMSEntity::getBukkitEntity)
                                                                           .map(x -> ((Player) x))
                                                                           .collect(Collectors.toList());
    }
}
