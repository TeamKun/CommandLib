package net.kunmc.lab.testplugin;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.util.ExceptionUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandSyntaxExceptionTest extends TestBase {
    public CommandSyntaxExceptionTest(Command command) {
        super(command);
    }

    @Override
    public List<String> build() {
        List<String> commands = new ArrayList<>();

        commands.addAll(testConvertCommandSyntaxException());

        return commands;
    }

    public List<String> testConvertCommandSyntaxException() {
        String name = getMethodName();
        String key = getKey();

        command.addChildren(new Command(name) {{
            argument(new Argument<Object>("a", BoolArgumentType.bool()) {
                {
                    applyOption(new Option<Object, CommandContext>().addUncaughtExceptionHandler((e, ctx) -> {
                        putResult(new TestResult(key, TestStatus.FAILED, ExceptionUtil.stackTraceToString(e)));
                    }));
                }

                @Override
                public Object cast(Object parsedArgument) {
                    return parsedArgument;
                }

                @Override
                protected Object parseImpl(CommandContext ctx) throws CommandSyntaxException {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh()
                                                                    .create(1, 2);
                }
            }, (a, ctx) -> {
                putResult(new TestResult(key, TestStatus.FAILED, "CommandSyntaxException was not thrown."));
            });
        }});
        putResult(new TestResult(key, TestStatus.SUCCEEDED, "Succeeded converting CommandSyntaxException"));

        return Lists.newArrayList(buildCommand(command, name + " true"));
    }
}
