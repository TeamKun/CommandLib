package net.kunmc.lab.testplugin;

import com.google.common.collect.Lists;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.*;
import net.kunmc.lab.commandlib.util.ExceptionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public final class ArgumentTest extends TestBase {
    public ArgumentTest(Command command) {
        super(command);
    }

    @Override
    public List<String> build() {
        List<String> commands = new ArrayList<>();

        commands.addAll(blockDataArgument());
        commands.addAll(booleanArgument());
        commands.addAll(doubleArgument());
        commands.addAll(enchantmentArgument());
        commands.addAll(entitiesArgument());
        commands.addAll(entityArgument());
        commands.addAll(enumArgument());
        commands.addAll(floatArgument());
        commands.addAll(integerArgument());
        commands.addAll(itemStackArgument());
        commands.addAll(literalArgument());
        commands.addAll(locationArgument());
        commands.addAll(nameableObjectArgument());
        commands.addAll(objectArgument());
        commands.addAll(offlinePlayerArgument());
        commands.addAll(offlinePlayersArgument());
        commands.addAll(particleArgument());
        commands.addAll(playerArgument());
        commands.addAll(playersArgument());
        commands.addAll(potionEffectArgument());
        commands.addAll(stringArgument());
        commands.addAll(teamArgument());
        commands.addAll(unparsedArgument());
        commands.addAll(uuidArgument());
        commands.addAll(uuidsArgument());

        return commands;
    }

    public List<String> blockDataArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new BlockDataArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " minecraft:stone"));
    }

    public List<String> booleanArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new BooleanArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " true"));
    }

    public List<String> doubleArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new DoubleArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " 1.0"));
    }

    public List<String> enchantmentArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new EnchantmentArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " minecraft:flame"));
    }

    public List<String> entitiesArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new EntitiesArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " @r"));
    }

    public List<String> entityArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new EntityArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " @r"));
    }

    public List<String> enumArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new EnumArgument<>("a", Material.class, option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " air"));
    }

    public List<String> floatArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new FloatArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " 1.0"));
    }

    public List<String> integerArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new IntegerArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " 1"));
    }

    public List<String> itemStackArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new ItemStackArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " minecraft:dirt"));
    }

    public List<String> literalArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new LiteralArgument("a", Lists.newArrayList("a"), option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " a"));
    }

    public List<String> locationArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new LocationArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " 0 0 0"));
    }

    public List<String> nameableObjectArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new NameableObjectArgument<>("a", Lists.newArrayList(() -> "a"), option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " a"));
    }

    public List<String> objectArgument() {
        String name = getMethodName();
        String key = getKey();

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new ObjectArgument<>("a", map, option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " a"));
    }

    public List<String> offlinePlayerArgument() {
        String name = getMethodName();
        String key = getKey();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayers()[0];

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new OfflinePlayerArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " " + offlinePlayer.getName()));
    }

    public List<String> offlinePlayersArgument() {
        String name = getMethodName();
        String key = getKey();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayers()[0];

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new OfflinePlayersArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " " + offlinePlayer.getName()));
    }

    public List<String> particleArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new ParticleArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " minecraft:flame"));
    }

    public List<String> playerArgument() {
        String name = getMethodName();
        String key = getKey();
        Player player = Bukkit.getOnlinePlayers()
                              .toArray(new Player[]{})[0];

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new PlayerArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " " + player.getName()));
    }

    public List<String> playersArgument() {
        String name = getMethodName();
        String key = getKey();
        Player player = Bukkit.getOnlinePlayers()
                              .toArray(new Player[]{})[0];

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new PlayersArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " " + player.getName()));
    }

    public List<String> potionEffectArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new PotionEffectArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " minecraft:speed"));
    }

    public List<String> stringArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new StringArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " a"));
    }

    public List<String> teamArgument() {
        String name = getMethodName();
        String key = getKey();

        Scoreboard scoreboard = Bukkit.getScoreboardManager()
                                      .getMainScoreboard();
        if (scoreboard.getTeams()
                      .isEmpty()) {
            scoreboard.registerNewTeam("test");
        }
        Team team = scoreboard.getTeams()
                              .toArray(new Team[]{})[0];

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new TeamArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " " + team.getName()));
    }

    public List<String> unparsedArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new UnparsedArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " a"));
    }

    public List<String> uuidArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new UUIDArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " " + UUID.randomUUID()));
    }


    public List<String> uuidsArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new UUIDsArgument("a", option -> {
                option.addUncaughtExceptionHandler((e, ctx) -> {
                    putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                });
            }), (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return Lists.newArrayList(buildCommand(command, name + " " + UUID.randomUUID()));
    }
}
