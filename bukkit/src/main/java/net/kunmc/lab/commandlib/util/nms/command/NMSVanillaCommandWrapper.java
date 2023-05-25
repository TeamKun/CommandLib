package net.kunmc.lab.commandlib.util.nms.command;

import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.util.nms.CraftBukkitClass;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.InvocationTargetException;

public class NMSVanillaCommandWrapper extends CraftBukkitClass {
    public NMSVanillaCommandWrapper() {
        super(null, "command.VanillaCommandWrapper");
    }

    public BukkitCommand createInstance(NMSCommandDispatcher dispatcher, CommandNode<?> command) {
        try {
            return ((BukkitCommand) clazz.getConstructor(dispatcher.getHandle()
                                                                   .getClass(), CommandNode.class)
                                         .newInstance(dispatcher.getHandle(), command));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
