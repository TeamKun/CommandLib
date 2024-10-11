package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.kunmc.lab.commandlib.util.bukkit.BukkitUtil;
import net.kunmc.lab.commandlib.util.bukkit.MinecraftVersion;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandDispatcher;
import net.kunmc.lab.commandlib.util.nms.command.NMSVanillaCommandWrapper;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public final class CommandLib implements Listener {
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
        this.plugin = Objects.requireNonNull(plugin);
        this.commands = Objects.requireNonNull(commands);
        for (Command command : commands) {
            Objects.requireNonNull(command);
        }

        enable();
        Bukkit.getPluginManager()
              .registerEvents(this, plugin);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void enable() {
        // 1ティック遅延させないとreload confirmの時にバグる
        new BukkitRunnable() {
            @Override
            public void run() {
                registeredCommands.addAll(new CommandNodeCreator<>(commands).build());
                registeredCommands.forEach(x -> {
                    try {
                        CommandMap commandMap = ((CommandMap) NMSCraftServer.create()
                                                                            .getValue("commandMap"));
                        Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                        knownCommandsField.setAccessible(true);
                        Map<String, org.bukkit.command.Command> knownCommands = ((Map<String, org.bukkit.command.Command>) knownCommandsField.get(
                                commandMap));

                        if (new MinecraftVersion(BukkitUtil.getMinecraftVersion()).isLessThan(new MinecraftVersion(
                                "1.21.0"))) {
                            NMSCommandDispatcher dispatcher = NMSCraftServer.create(plugin.getServer())
                                                                            .getServer()
                                                                            .getCommandDispatcher();
                            RootCommandNode root = dispatcher.getBrigadier()
                                                             .getRoot();

                            root.addChild(x);
                            knownCommands.put(x.getName(),
                                              NMSVanillaCommandWrapper.create()
                                                                      .createInstance(dispatcher, x));

                            root.getChild("execute")
                                .getChild("run")
                                .getRedirect()
                                .addChild(x);
                        } else {
                            CommandNode shadowBrigNode = (CommandNode) Class.forName(
                                                                                    "io.papermc.paper.command.brigadier.ShadowBrigNode")
                                                                            .getConstructor(CommandNode.class)
                                                                            .newInstance(x);
                            CommandDispatcher dispatcher = ((CommandDispatcher) knownCommands.getClass()
                                                                                             .getDeclaredMethod(
                                                                                                     "getDispatcher")
                                                                                             .invoke(knownCommands));
                            dispatcher.getRoot()
                                      .addChild(shadowBrigNode);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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

    @SuppressWarnings("rawtypes")
    public void unregister() {
        RootCommandNode root = NMSCraftServer.create(plugin.getServer())
                                             .getServer()
                                             .getCommandDispatcher()
                                             .getBrigadier()
                                             .getRoot();
        try {
            CommandMap commandMap = ((CommandMap) NMSCraftServer.create()
                                                                .getValue("commandMap"));
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            Map<String, org.bukkit.command.Command> knownCommands = ((Map<String, org.bukkit.command.Command>) knownCommandsField.get(
                    commandMap));
            for (String s : registeredCommands.stream()
                                              .map(CommandNode::getName)
                                              .collect(Collectors.toList())) {
                removeCommand(root, s);
                removeCommand(root, "minecraft:" + s);
                knownCommands.remove(s);
                knownCommands.remove("minecraft:" + s);

                removeCommand(root.getChild("execute")
                                  .getChild("run")
                                  .getRedirect(), s);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        registeredCommands.clear();
        HandlerList.unregisterAll(this);

        commands.stream()
                .flatMap(x -> x.permissions()
                               .stream())
                .forEach(Bukkit.getPluginManager()::removePermission);

        Bukkit.getOnlinePlayers()
              .forEach(Player::updateCommands);
    }

    @SuppressWarnings("rawtypes")
    private static void removeCommand(CommandNode<?> commandNode, String name) throws Exception {
        Class<?> clazz = CommandNode.class;

        Field children = clazz.getDeclaredField("children");
        children.setAccessible(true);
        ((Map) children.get(commandNode)).remove(name);

        Field literals = clazz.getDeclaredField("literals");
        literals.setAccessible(true);
        ((Map) literals.get(commandNode)).remove(name);

        Field arguments = clazz.getDeclaredField("arguments");
        arguments.setAccessible(true);
        ((Map) arguments.get(commandNode)).remove(name);
    }
}
