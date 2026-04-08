package net.kunmc.lab.commandlib.nms.command;

import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandListenerWrapper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class MockNMSCommandListenerWrapper extends NMSCommandListenerWrapper {
    public MockNMSCommandListenerWrapper(Object handle) {
        super(handle, "Mock");
    }

    @Override
    public CommandSender getBukkitSender() {
        return CommandTester.getCurrentCommandSender();
    }

    @Override
    public Entity getBukkitEntity() {
        CommandSender sender = CommandTester.getCurrentCommandSender();
        return sender instanceof Entity ? (Entity) sender : null;
    }

    @Override
    public World getBukkitWorld() {
        CommandSender sender = CommandTester.getCurrentCommandSender();
        return sender instanceof Entity ? ((Entity) sender).getWorld() : null;
    }

    @Override
    public Location getBukkitLocation() {
        CommandSender sender = CommandTester.getCurrentCommandSender();
        return sender instanceof Entity ? ((Entity) sender).getLocation() : null;
    }
}
