package net.kunmc.lab.commandlib;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.v1_16_R3.CommandDispatcher;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CommandLib implements Listener {
    private final Plugin plugin;
    private final List<Command> cmds;
    private final List<CommandNode<CommandListenerWrapper>> registeredCommands = new ArrayList<>();

    public static CommandLib register(@NotNull Plugin plugin, @NotNull Command cmd, @NotNull Command... cmds) {
        List<Command> list = new ArrayList<Command>() {{
            add(cmd);
            addAll(Arrays.asList(cmds));
        }};

        return new CommandLib(plugin, list);
    }

    static int executeWithStackTrace(CommandContext ctx, ContextAction contextAction) {
        try {
            contextAction.accept(ctx);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            ctx.sendFailure("An unexpected error occurred trying to execute that command.");
            ctx.sendFailure("Check the console for details.");
            return 0;
        }
    }

    private CommandLib(Plugin plugin, List<Command> cmds) {
        this.plugin = plugin;
        this.cmds = cmds;

        enable();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void enable() {
        registeredCommands.addAll(cmds.stream()
                .map(Command::toCommandNodes)
                .reduce(new ArrayList<>(), (x, y) -> {
                    x.addAll(y);
                    return x;
                }));

        CommandDispatcher dispatcher = ((CraftServer) plugin.getServer()).getServer().getCommandDispatcher();
        RootCommandNode<CommandListenerWrapper> root = dispatcher.a().getRoot();
        registeredCommands.forEach(c -> {
            root.addChild(c);
            Bukkit.getCommandMap().getKnownCommands().put(c.getName(), new VanillaCommandWrapper(dispatcher, c));
        });
    }

    @EventHandler
    private void onPluginDisable(PluginDisableEvent e) {
        if (!e.getPlugin().equals(plugin)) {
            return;
        }

        unregister();
    }

    public void unregister() {
        RootCommandNode<CommandListenerWrapper> root = ((CraftServer) plugin.getServer()).getServer().getCommandDispatcher().dispatcher().getRoot();
        Map<String, org.bukkit.command.Command> knownCommands = Bukkit.getCommandMap().getKnownCommands();
        registeredCommands.stream()
                .map(CommandNode::getName)
                .forEach(s -> {
                    root.removeCommand(s);
                    root.removeCommand("minecraft:" + s);
                    knownCommands.remove(s);
                    knownCommands.remove("minecraft:" + s);
                });

        registeredCommands.clear();
        HandlerList.unregisterAll(this);

        Bukkit.getPluginManager().getPermissions().forEach(p -> {
            System.out.println(p.getName());
        });

        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }
}
