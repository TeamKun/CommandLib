package net.kunmc.lab.testplugin;

import com.google.common.collect.Lists;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.commandlib.argument.*;
import net.kunmc.lab.commandlib.exception.InvalidArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

public final class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandLib.register(this, new Command("commandlibtest2") {{
            argument(new IntegerArgument("n", option -> option.suggestionAction(sb -> {
                sb.getContext()
                  .sendSuccess(sb.getLatestInput());
            })), new StringArgument("s", option -> option.suggestionAction(sb -> {
                sb.getContext()
                  .sendSuccess(sb.getLatestInput());
            }), StringArgument.Type.PHRASE), (n, s, ctx) -> {
                ctx.sendSuccess(n);
            });
        }}, new Command("commandlibtest") {{
            addAliases("commandlibtestalias");
            setDescription("test command");
            setPermission(PermissionDefault.TRUE);

            addPrerequisite(ctx -> {
                ctx.sendMessage("prerequisite1");
                return true;
            });
            addPrerequisite(ctx -> {
                ctx.sendMessage("prerequisite2");
                return Bukkit.getCurrentTick() % 2 == 0;
            });
            addPrerequisite(ctx -> {
                ctx.sendMessage("prerequisite3");
                return true;
            });

            addPreprocess(ctx -> {
                ctx.sendWarn("preprocess1");
            });
            addPreprocess(ctx -> {
                ctx.sendWarn("preprocess2");
                return Bukkit.getCurrentTick() % 4 == 0;
            });
            addPreprocess(ctx -> {
                ctx.sendWarn("preprocess3");
            });


            addChildren(new Command("additionalparse") {{
                argument(new UUIDArgument("target", option -> {
                    option.suggestionAction(sb -> sb.suggest("hogehoge"))
                          .additionalParser((ctx, input) -> {
                              if (input.equals("hogehoge") && !Bukkit.getOnlinePlayers()
                                                                     .isEmpty()) {
                                  return new ArrayList<>(Bukkit.getOnlinePlayers()).get(0)
                                                                                   .getUniqueId();
                              }
                              return null;
                          });
                }), (material, ctx) -> {
                    ctx.sendSuccess(material);
                });
            }}, new Command("inheritTest") {{
                execute(ctx -> ctx.sendSuccess("inheritTest"));
                argument(new IntegerArgument("n"), (n, ctx) -> ctx.sendSuccess(n));
            }}, new Command("noInheritTest") {{
                setInheritParentPreprocess(false);
                execute(ctx -> ctx.sendSuccess("noInheritTest"));
                argument(new IntegerArgument("n"), (n, ctx) -> ctx.sendSuccess(n));
            }}, new Command("inheritAndComposeTest") {{
                addPreprocess(ctx -> {
                    ctx.sendFailure("compose pre");
                });
                execute(ctx -> ctx.sendSuccess("compose"));
                argument(new IntegerArgument("n"), (n, ctx) -> ctx.sendSuccess(n));
            }}, new Command("test") {{
                addAliases("alias");
                argument(new PlayerArgument("target", option -> option.suggestionAction(sb -> sb.suggest("aiueo"))),
                         (p, ctx) -> {
                             ctx.sendSuccess(p.getName());
                         });
            }}, new Command("var") {{
                argument(new IntegerArgument("integer"), (integer, ctx) -> ctx.sendSuccess(integer));
                argument(new IntegerArgument("integer"),
                         new EnumArgument<>("enum", Material.class),
                         (integer, e, ctx) -> ctx.sendSuccess(integer + e.name()));
            }}, new Command("a") {{
                setDescription("a command");
                setPermission(PermissionDefault.OP);
                argument(new OfflinePlayersArgument("offlines"),
                         (offlinePlayers, ctx) -> ctx.sendSuccess(offlinePlayers));
            }}, new Command("b") {{
                setPermission(PermissionDefault.TRUE);
                addChildren(new Command("child") {{
                    argument(new UUIDsArgument("uuids"),
                             new DoubleArgument("d"),
                             new StringArgument("str"),
                             (uuids, d, str, ctx) -> {
                                 ctx.sendSuccess(ctx.getParsedArgs());
                             });
                }}, new Command("child2") {{
                }});

                argument(new StringArgument("str", option -> option.suggestionAction(sb -> sb.suggest("test"))),
                         (d, ctx) -> ctx.sendSuccess(d));
            }}, new Command("c") {{
                argument(new EnumArgument<>("enu", Material.class, option -> {
                    option.filter(x -> {
                        if (!x.isBlock()) {
                            throw new InvalidArgumentException("only block");
                        }
                    });
                }), (e, ctx) -> {
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
