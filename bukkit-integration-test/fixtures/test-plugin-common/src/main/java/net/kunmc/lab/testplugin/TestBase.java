package net.kunmc.lab.testplugin;

import net.kunmc.lab.commandlib.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TestBase {
    private final Map<String, TestResult> resultMap = new ConcurrentHashMap<>();
    protected final Command command;

    public TestBase(Command command) {
        this.command = command;
    }

    public abstract List<String> build();

    protected final void putResult(TestResult result) {
        resultMap.put(result.key(), result);
    }

    protected final String buildCommand(Command command, String subCommand) {
        return command.name() + " " + subCommand;
    }

    protected final String getMethodName() {
        return getMethodName(1);
    }

    protected final String getMethodName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread()
                                              .getStackTrace();
        return ste[2 + depth].getMethodName();
    }

    protected final String getKey() {
        return getClass().getSimpleName() + "." + getMethodName(1);
    }

    public final List<TestResult> results() {
        return new ArrayList<>(Collections.unmodifiableCollection(resultMap.values()));
    }

    public final void clearResults() {
        resultMap.clear();
    }
}
