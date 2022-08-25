package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandLib implements Listener {
    private final Plugin plugin;
    private final Collection<? extends Command> commands;
    private final List<CommandNode<CommandListenerWrapper>> registeredCommands = new ArrayList<>();

    public static CommandLib register(@NotNull Plugin plugin, @NotNull Command command, @NotNull Command... commands) {
        return register(plugin, Lists.asList(command, commands));
    }

    public static CommandLib register(@NotNull Plugin plugin, @NotNull Collection<? extends Command> commands) {
        return new CommandLib(plugin, commands);
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

    private CommandLib(Plugin plugin, Collection<? extends Command> commands) {
        this.plugin = plugin;
        this.commands = commands;

        enable();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void enable() {
        registeredCommands.addAll(commands.stream()
                .map(Command::toCommandNodes)
                .reduce(new ArrayList<>(), (x, y) -> {
                    x.addAll(y);
                    return x;
                }));

        CommandDispatcher dispatcher = ((CraftServer) plugin.getServer()).getServer().getCommandDispatcher();
        RootCommandNode<CommandListenerWrapper> root = dispatcher.a().getRoot();
        registeredCommands.forEach(x -> {
            root.addChild(x);
            Bukkit.getCommandMap().getKnownCommands().put(x.getName(), new VanillaCommandWrapper(dispatcher, x));
        });

        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
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

        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }
}
