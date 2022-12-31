package net.kunmc.lab.testplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.commandlib.argument.DoubleArgument;
import net.kunmc.lab.commandlib.argument.StringArgument;
import net.kunmc.lab.commandlib.argument.UUIDArgument;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandLib.register(this, new Command("commandlibtest") {{
            execute(ctx -> {
                ctx.sendSuccess("test msg");
                ctx.sendSuccess(ctx.getArgs());
            });
            argument(new UUIDArgument("target"),
                     new DoubleArgument("double"),
                     new StringArgument("str", StringArgument.Type.PHRASE),
                     (target, d, s, ctx) -> {
                         ctx.sendSuccess(ctx.getHandle()
                                            .getInput());
                         ctx.sendSuccess(ctx.getArgs());
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
