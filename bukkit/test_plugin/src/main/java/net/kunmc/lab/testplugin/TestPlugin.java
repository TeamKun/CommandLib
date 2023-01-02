package net.kunmc.lab.testplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.commandlib.argument.DoubleArgument;
import net.kunmc.lab.commandlib.argument.PlayerArgument;
import net.kunmc.lab.commandlib.argument.StringArgument;
import net.kunmc.lab.commandlib.argument.UUIDArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandLib.register(this, new Command("commandlibtest") {{
            addAliases("commandlibtestalias");
            setDescription("test command");

            addChildren(new Command("a") {{
                setDescription("a command");
                argument(new PlayerArgument("player"), (player, ctx) -> ctx.sendSuccess(player.getName()));
            }}, new Command("b") {{
                addChildren(new Command("child") {{
                    execute(ctx -> ctx.sendSuccess("b child"));
                }});
            }});

            argument(new UUIDArgument("target"),
                     new DoubleArgument("double"),
                     new StringArgument("str", StringArgument.Type.PHRASE),
                     (target, d, s, ctx) -> {
                         ctx.sendSuccess(ctx.getHandle()
                                            .getInput());
                         ctx.sendSuccess(ctx.getArgs());
                         if (ctx.getSender() instanceof Player) {
                             ctx.sendFailure(ctx.getHandle()
                                                .getSource()
                                                .getBukkitWorld()
                                                .getName());
                         }
                     });
        }});
    }

    @Override
    public void onDisable() {
    }

    public static void print(Object obj) {
        if (Objects.equals(System.getProperty("plugin.env"), "DEV")) {
            System.out.printf("[%s] %s%n", TestPlugin.class.getSimpleName(), obj);
        }
    }

    public static void broadcast(Object obj) {
        if (Objects.equals(System.getProperty("plugin.env"), "DEV")) {
            Bukkit.broadcastMessage(String.format("[%s] %s", TestPlugin.class.getSimpleName(), obj));
        }
    }
}
