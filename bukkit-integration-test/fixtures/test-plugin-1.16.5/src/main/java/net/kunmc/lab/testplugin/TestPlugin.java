package net.kunmc.lab.testplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.commandlib.argument.EnumArgument;
import net.kunmc.lab.commandlib.argument.IntegerArgument;
import net.kunmc.lab.commandlib.argument.PlayerArgument;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public final class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        new TestMain(this).register();

        CommandLib.register(this, new Command("a") {{
            addChildren(new Command("hoge") {{
                description("hoge command");
                execute(ctx -> ctx.sendSuccess("hoge"));
                argument(builder -> {
                    builder.integerArgument("n")
                           .execute(ctx -> {
                               ctx.sendSuccess("hoge" + ctx.getParsedArg("n"));
                           });
                });
            }});

            argument(builder -> {
                builder.integerArgument("n");
            });
            argument(new IntegerArgument("n"), new PlayerArgument("p")).execute((n, p, ctx) -> {
                                                                           ctx.sendSuccess(n);
                                                                           ctx.sendSuccess(p);
                                                                       })
                                                                       .child((nArg, pArg) -> new Command("sub") {{
                                                                           execute(ctx -> {
                                                                               ctx.sendSuccess("sub");
                                                                               ctx.sendSuccess(ctx.getParsedArg(nArg));
                                                                               ctx.sendSuccess(ctx.getParsedArg(pArg));
                                                                           });
                                                                           argument(builder -> {
                                                                               builder.boolArgument("b");
                                                                           });
                                                                           argument(builder -> {
                                                                               builder.floatArgument("float");
                                                                           }).child((new Command("sub") {{
                                                                               execute(ctx -> {
                                                                                   ctx.sendSuccess("sub sub");
                                                                                   ctx.sendSuccess(ctx.getParsedArg(nArg));
                                                                                   ctx.sendSuccess(ctx.getParsedArg(pArg));
                                                                                   ctx.sendSuccess(ctx.getParsedArg(
                                                                                           "float"));
                                                                               });
                                                                           }}));
                                                                       }})
                                                                       .child((nArg, pArg) -> {
                                                                           return new Command("sub2") {{
                                                                               description("sub2");
                                                                               argument(new EnumArgument<>("enum",
                                                                                                           Material.class)).execute(
                                                                                       (material, ctx) -> {
                                                                                           ctx.sendSuccess(material);
                                                                                           ctx.sendSuccess(ctx.getParsedArg(
                                                                                                   nArg) + ":" + ctx.getParsedArg(
                                                                                                   pArg));
                                                                                       });
                                                                           }};
                                                                       })
                                                                       .description("np desc");
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
