package net.kunmc.lab.commandlib.util.nms.command.v1_16_0;

import com.mojang.brigadier.tree.CommandNode;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.command.NMSVanillaCommandWrapper;
import org.bukkit.command.defaults.BukkitCommand;

public class NMSVanillaCommandWrapper_v1_16_0 extends NMSVanillaCommandWrapper {
    public NMSVanillaCommandWrapper_v1_16_0() {
        super(null, "command.VanillaCommandWrapper");
    }

    @Override
    public BukkitCommand createInstance(NMSCommandDispatcher dispatcher, CommandNode<?> command) {
        return ((BukkitCommand) newInstance(new Class[]{dispatcher.getHandle().getClass(), CommandNode.class},
                                            new Object[]{dispatcher.getHandle(), command}));
    }
}
