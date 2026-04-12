package net.kunmc.lab.testplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.CommandOption;
import net.kunmc.lab.commandlib.Options;
import net.kunmc.lab.commandlib.argument.StringArgument;

import java.util.ArrayList;
import java.util.List;

public final class OptionTest extends TestBase {
    public OptionTest(Command command) {
        super(command);
    }

    @Override
    public List<String> build() {
        List<String> commands = new ArrayList<>();

        commands.addAll(flagAndValueOptions());
        commands.addAll(defaultOptionValues());
        commands.addAll(combinedShortFlags());
        commands.addAll(valueOptionWithoutValue());
        commands.addAll(optionAfterArgument());

        return commands;
    }

    public List<String> flagAndValueOptions() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f')
                                                                         .description("Force execution"));
            CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100)
                                                                         .description("Maximum count"));
            CommandOption<String, CommandContext> format = option(Options.string("format", 'F', "text")
                                                                         .description("Output format"));

            argument(new StringArgument("str", StringArgument.Type.WORD), (target, ctx) -> {
                putResult(key,
                          ctx.getOption(force) + ":" + ctx.getOption(limit) + ":" + ctx.getOption(format) + ":" + target,
                          "true:20:json:alex");
            });
        }});

        return List.of(buildCommand(command, name + " -f -n 20 --format json alex"));
    }

    public List<String> defaultOptionValues() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100));
            CommandOption<String, CommandContext> format = option(Options.string("format", 'F', "text"));

            argument(new StringArgument("target", StringArgument.Type.WORD), (target, ctx) -> {
                putResult(key,
                          ctx.getOption(force) + ":" + ctx.getOption(limit) + ":" + ctx.getOption(format) + ":" + target,
                          "false:10:text:alex");
            });
        }});

        return List.of(buildCommand(command, name + " alex"));
    }

    public List<String> combinedShortFlags() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.FAILED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            CommandOption<Boolean, CommandContext> verbose = option(Options.flag("verbose", 'v'));

            argument(new StringArgument("target", StringArgument.Type.WORD), (target, ctx) -> {
                putResult(key, ctx.getOption(force) + ":" + ctx.getOption(verbose) + ":" + target, "true:true:alex");
            });
        }});

        return List.of(buildCommand(command, name + " -fv alex"));
    }

    public List<String> valueOptionWithoutValue() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.SUCCEEDED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            CommandOption<String, CommandContext> format = option(Options.string("format", 'F', "text"));

            argument(new StringArgument("target", StringArgument.Type.WORD), (target, ctx) -> {
                putResult(new TestResult(key,
                                         TestStatus.FAILED,
                                         "Command was executed with format=" + ctx.getOption(format)));
            });
        }});

        return List.of(buildCommand(command, name + " --format"));
    }

    public List<String> optionAfterArgument() {
        String name = getMethodName();
        String key = getKey();

        putResult(new TestResult(key, TestStatus.SUCCEEDED, "Command was not executed."));
        command.addChildren(new Command(name) {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));

            argument(new StringArgument("target", StringArgument.Type.WORD), (target, ctx) -> {
                putResult(new TestResult(key,
                                         TestStatus.FAILED,
                                         "Command was executed with force=" + ctx.getOption(force)));
            });
        }});

        return List.of(buildCommand(command, name + " alex -f"));
    }

}
