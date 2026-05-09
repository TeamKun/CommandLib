package net.kunmc.lab.testplugin;

public interface CommandDispatchErrorHook {
    void onCommandDispatchError(String commandLine, CommandDispatchResult dispatchResult);
}
