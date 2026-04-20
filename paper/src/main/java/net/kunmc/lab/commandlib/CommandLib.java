package net.kunmc.lab.commandlib;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entry point for registering CommandLib commands on Paper 1.20.6+.
 *
 * <p>Call {@link #register} in your plugin's constructor or {@code onLoad()} so the
 * lifecycle event handler is installed before the server processes commands.
 *
 * <pre>{@code
 * public class MyPlugin extends JavaPlugin {
 *     public MyPlugin() {
 *         CommandLib.register(this, new MyCommand());
 *     }
 * }
 * }</pre>
 */
@SuppressWarnings("UnstableApiUsage")
public final class CommandLib {
    private CommandLib() {
    }

    public static void register(@NotNull Plugin plugin, @NotNull Command command, @NotNull Command... commands) {
        List<Command> list = new ArrayList<>();
        list.add(command);
        Collections.addAll(list, commands);
        register(plugin, list);
    }

    public static void register(@NotNull Plugin plugin, @NotNull Collection<? extends Command> commands) {
        register(plugin, plugin.getName().toLowerCase() + ".command", commands);
    }

    public static void register(@NotNull Plugin plugin,
                                @NotNull String permissionPrefix,
                                @NotNull Command command,
                                @NotNull Command... commands) {
        List<Command> list = new ArrayList<>();
        list.add(command);
        Collections.addAll(list, commands);
        register(plugin, permissionPrefix, list);
    }

    public static void register(@NotNull Plugin plugin,
                                @NotNull String permissionPrefix,
                                @NotNull Collection<? extends Command> commands) {
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(permissionPrefix);
        Objects.requireNonNull(commands);
        if (permissionPrefix.isEmpty()) {
            throw new IllegalArgumentException("permissionPrefix must not be empty");
        }
        commands.forEach(Objects::requireNonNull);

        registerPermissions(plugin, permissionPrefix, commands);

        LifecycleEventManager<Plugin> lifecycleManager = plugin.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands registrar = event.registrar();
            List<LiteralCommandNode<CommandSourceStack>> nodes =
                    new CommandNodeCreator<>(commands, permissionPrefix).build();
            nodes.forEach(node -> registrar.register(node, ""));
        });
    }

    private static void registerPermissions(Plugin plugin,
                                            String permissionPrefix,
                                            Collection<? extends Command> commands) {
        commands.stream()
                .flatMap(c -> c.permissions(permissionPrefix).stream())
                .forEach(permission -> {
                    removePermissionIfPresent(plugin, permission.getName());
                    plugin.getServer().getPluginManager().addPermission(permission);
                });
    }

    private static void removePermissionIfPresent(Plugin plugin, String name) {
        Permission existing = plugin.getServer().getPluginManager().getPermission(name);
        if (existing != null) {
            existing.setDefault(PermissionDefault.FALSE);
            plugin.getServer().getPluginManager().recalculatePermissionDefaults(existing);
            plugin.getServer().getPluginManager().removePermission(name);
        }
    }
}
