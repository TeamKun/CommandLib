package net.kunmc.lab.testplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.*;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.util.*;

public final class ArgumentTest extends TestBase {
    private final String playerName;

    public ArgumentTest(Command command, String playerName) {
        super(command);
        this.playerName = playerName;
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
            argument(new BlockDataArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return List.of(buildCommand(command, name + " minecraft:stone"));
    }

    public List<String> booleanArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new BooleanArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.toString(), "true");
            });
        }});

        return List.of(buildCommand(command, name + " true"));
    }

    public List<String> doubleArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new DoubleArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.toString(), "1.0");
            });
        }});

        return List.of(buildCommand(command, name + " 1.0"));
    }

    public List<String> enchantmentArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new EnchantmentArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return List.of(buildCommand(command, name + " minecraft:flame"));
    }

    public List<String> entitiesArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new EntitiesArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, Integer.toString(a.size()), "1");
            });
        }});

        return List.of(buildCommand(command, name + " @r"));
    }

    public List<String> entityArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new EntityArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return List.of(buildCommand(command, name + " @r"));
    }

    public List<String> enumArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new EnumArgument<>("a", Material.class).addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.toString(), "AIR");
            });
        }});

        return List.of(buildCommand(command, name + " air"));
    }

    public List<String> floatArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new FloatArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.toString(), "1.0");
            });
        }});

        return List.of(buildCommand(command, name + " 1.0"));
    }

    public List<String> integerArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new IntegerArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.toString(), "1");
            });
        }});

        return List.of(buildCommand(command, name + " 1"));
    }

    public List<String> itemStackArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new ItemStackArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return List.of(buildCommand(command, name + " minecraft:dirt"));
    }

    public List<String> literalArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new LiteralArgument("a", List.of("a")).addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a, "a");
            });
        }});

        return List.of(buildCommand(command, name + " a"));
    }

    public List<String> locationArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new LocationArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return List.of(buildCommand(command, name + " 0 0 0"));
    }

    public List<String> nameableObjectArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new NameableObjectArgument<>("a", List.of(() -> "a")).addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.tabCompleteName(), "a");
            });
        }});

        return List.of(buildCommand(command, name + " a"));
    }

    public List<String> objectArgument() {
        String name = getMethodName();
        String key = getKey();

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new ObjectArgument<>("a", map).addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.toString(), "1");
            });
        }});

        return List.of(buildCommand(command, name + " a"));
    }

    public List<String> offlinePlayerArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new OfflinePlayerArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.getName(), playerName);
            });
        }});

        return List.of(buildCommand(command, name + " " + playerName));
    }

    public List<String> offlinePlayersArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new OfflinePlayersArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key,
                          a.stream()
                           .map(OfflinePlayer::getName)
                           .collect(java.util.stream.Collectors.joining(",")),
                          playerName);
            });
        }});

        return List.of(buildCommand(command, name + " " + playerName));
    }

    public List<String> particleArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new ParticleArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return List.of(buildCommand(command, name + " minecraft:flame"));
    }

    public List<String> playerArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new PlayerArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.getName(), playerName);
            });
        }});

        return List.of(buildCommand(command, name + " " + playerName));
    }

    public List<String> playersArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new PlayersArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key,
                          a.stream()
                           .map(x -> x.getName())
                           .collect(java.util.stream.Collectors.joining(",")),
                          playerName);
            });
        }});

        return List.of(buildCommand(command, name + " " + playerName));
    }

    public List<String> potionEffectArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new PotionEffectArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(new TestResult(key, TestStatus.SUCCEEDED, a.toString()));
            });
        }});

        return List.of(buildCommand(command, name + " minecraft:speed"));
    }

    public List<String> stringArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new StringArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a, "a");
            });
        }});

        return List.of(buildCommand(command, name + " a"));
    }

    public List<String> teamArgument() {
        String name = getMethodName();
        String key = getKey();

        String teamName = "test";
        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new TeamArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.getName(), teamName);
            });
        }});

        return List.of(buildCommand(command, name + " " + teamName));
    }

    public List<String> unparsedArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new UnparsedArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a, "a");
            });
        }});

        return List.of(buildCommand(command, name + " a"));
    }

    public List<String> uuidArgument() {
        String name = getMethodName();
        String key = getKey();
        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new UUIDArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key, a.toString(), uuid.toString());
            });
        }});

        return List.of(buildCommand(command, name + " " + uuid));
    }


    public List<String> uuidsArgument() {
        String name = getMethodName();
        String key = getKey();
        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            argument(new UUIDsArgument("a").addUncaughtExceptionHandler((e, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
            })).execute((a, ctx) -> {
                putResult(key,
                          a.toString(),
                          List.of(uuid)
                              .toString());
            });
        }});

        return List.of(buildCommand(command, name + " " + uuid));
    }
}
