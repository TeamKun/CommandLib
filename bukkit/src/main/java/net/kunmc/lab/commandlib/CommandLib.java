package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.command.NMSVanillaCommandWrapper;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class CommandLib extends AbstractCommandLib implements Listener {
    private final Plugin plugin;
    private final Collection<? extends Command> commands;
    private final List<CommandNode<?>> registeredCommands = new ArrayList<>();

    public static CommandLib register(@NotNull Plugin plugin, @NotNull Command command, @NotNull Command... commands) {
        return register(plugin, Lists.asList(command, commands));
    }

    public static CommandLib register(@NotNull Plugin plugin, @NotNull Collection<? extends Command> commands) {
        return new CommandLib(plugin, commands);
    }

    private CommandLib(Plugin plugin, Collection<? extends Command> commands) {
        this.plugin = plugin;
        this.commands = commands;

        enable();
        Bukkit.getPluginManager()
              .registerEvents(this, plugin);
    }

    private void enable() {
        // 1ティック遅延させないとreload confirmの時にバグる
        new BukkitRunnable() {
            @Override
            public void run() {
                registeredCommands.addAll(new CommandNodeCreator<>(commands).build());
                NMSCommandDispatcher dispatcher = new NMSCraftServer(plugin.getServer()).getServer()
                                                                                        .getCommandDispatcher();
                RootCommandNode root = dispatcher.getBrigadier()
                                                 .getRoot();
                registeredCommands.forEach(x -> {
                    root.addChild(x);
                    Bukkit.getCommandMap()
                          .getKnownCommands()
                          .put(x.getName(), new NMSVanillaCommandWrapper().createInstance(dispatcher, x));

                    root.getChild("execute")
                        .getChild("run")
                        .getRedirect()
                        .addChild(x);
                });

                commands.stream()
                        .flatMap(x -> x.permissions()
                                       .stream())
                        .forEach(Bukkit.getPluginManager()::addPermission);

                Bukkit.getOnlinePlayers()
                      .forEach(Player::updateCommands);
            }
        }.runTask(plugin);
    }

    @EventHandler
    private void onPluginDisable(PluginDisableEvent e) {
        if (!e.getPlugin()
              .equals(plugin)) {
            return;
        }

        unregister();
    }

    public void unregister() {
        RootCommandNode root = new NMSCraftServer(plugin.getServer()).getServer()
                                                                     .getCommandDispatcher()
                                                                     .getBrigadier()
                                                                     .getRoot();
        Map<String, org.bukkit.command.Command> knownCommands = Bukkit.getCommandMap()
                                                                      .getKnownCommands();
        registeredCommands.stream()
                          .map(CommandNode::getName)
                          .forEach(s -> {
                              removeCommand(root, s);
                              removeCommand(root, "minecraft:" + s);
                              knownCommands.remove(s);
                              knownCommands.remove("minecraft:" + s);

                              removeCommand(root.getChild("execute")
                                                .getChild("run")
                                                .getRedirect(), s);
                          });

        registeredCommands.clear();
        HandlerList.unregisterAll(this);

        commands.stream()
                .flatMap(x -> x.permissions()
                               .stream())
                .forEach(Bukkit.getPluginManager()::removePermission);

        Bukkit.getOnlinePlayers()
              .forEach(Player::updateCommands);
    }

    private static void removeCommand(CommandNode<?> commandNode, String name) {
        Class<?> clazz = CommandNode.class;

        try {
            Field children = clazz.getDeclaredField("children");
            children.setAccessible(true);
            ((Map) children.get(commandNode)).remove(name);

            Field literals = clazz.getDeclaredField("literals");
            literals.setAccessible(true);
            ((Map) literals.get(commandNode)).remove(name);

            Field arguments = clazz.getDeclaredField("arguments");
            arguments.setAccessible(true);
            ((Map) arguments.get(commandNode)).remove(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
