package net.kunmc.lab.testplugin;

import com.google.common.collect.Lists;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.commandlib.argument.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
                argument(new OfflinePlayersArgument("offlines"),
                         (offlinePlayers, ctx) -> ctx.sendSuccess(offlinePlayers));
            }}, new Command("b") {{
                addChildren(new Command("child") {{
                    argument(new UUIDsArgument("uuids"),
                             new DoubleArgument("d"),
                             new StringArgument("str"),
                             (uuids, d, str, ctx) -> {
                                 ctx.sendSuccess(ctx.getParsedArgs());
                             });
                }});
            }}, new Command("c") {{
                argument(new EnumArgument<>("enu", Material.class), (e, ctx) -> {
                    ctx.sendMessageWithOption(e, option -> {
                    });
                    ctx.sendMessageWithOption(e, option -> option.rgb(0));
                });
            }});

            argument(new LiteralArgument("literals", Lists.newArrayList("hoge", "fuga")), (literal, ctx) -> {
                ctx.sendSuccess(literal);
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
