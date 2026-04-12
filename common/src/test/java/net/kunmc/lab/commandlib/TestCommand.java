package net.kunmc.lab.commandlib;

class TestCommand extends CommonCommand<TestCommandContext, TestArgumentBuilder, TestCommand> {
    TestCommand(String name) {
        super(name);
    }
}
