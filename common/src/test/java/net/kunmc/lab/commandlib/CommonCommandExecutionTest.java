package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.CommonStringArgument;
import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommonCommandExecutionTest {
    @Test
    void command_executes_without_arguments() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("ping") {{
            execute(ctx -> ctx.sendMessage("pong"));
        }});

        TestCommandContext ctx = runner.execute("ping");

        assertThat(ctx.messages()).containsExactly("pong");
    }

    @Test
    void alias_executes_same_command_tree() throws Exception {
        TestCommand command = new TestCommand("message") {{
            addAliases("msg");
            argument(new CommonStringArgument<>("body"), (body, ctx) -> {
                ctx.sendMessage(body);
            });
        }};
        TestCommandRunner runner = new TestCommandRunner(command);

        TestCommandContext ctx = runner.execute("msg hello");

        assertThat(ctx.messages()).containsExactly("hello");
    }

    @Test
    void child_command_executes_from_parent_tree() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("game") {{
            execute(ctx -> ctx.sendMessage("root"));
            addChildren(new TestCommand("start") {{
                execute(ctx -> ctx.sendMessage("started"));
            }});
        }});

        TestCommandContext root = runner.execute("game");
        TestCommandContext child = runner.execute("game start");

        assertThat(root.messages()).containsExactly("root");
        assertThat(child.messages()).containsExactly("started");
    }

    @Test
    void child_command_executes_with_its_own_arguments() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("game") {{
            addChildren(new TestCommand("start") {{
                argument(new CommonStringArgument<>("target"), (target, ctx) -> {
                    ctx.sendMessage("started:" + target);
                });
            }});
        }});

        TestCommandContext ctx = runner.execute("game start Alex");

        assertThat(ctx.messages()).containsExactly("started:Alex");
    }

    @Test
    void child_alias_executes_child_command() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("game") {{
            addChildren(new TestCommand("start") {{
                addAliases("run");
                execute(ctx -> ctx.sendMessage("started"));
            }});
        }});

        TestCommandContext ctx = runner.execute("game run");

        assertThat(ctx.messages()).containsExactly("started");
    }

    @Test
    void longest_argument_chain_is_selected_first() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("set") {{
            argument(new CommonStringArgument<>("key"), (key, ctx) -> {
                ctx.sendMessage("one:" + key);
            });
            argument(new CommonStringArgument<>("key"),
                     new CommonStringArgument<>("value"),
                     (key, value, ctx) -> {
                         ctx.sendMessage("two:" + key + ":" + value);
                     });
        }});

        TestCommandContext ctx = runner.execute("set name Steve");

        assertThat(ctx.messages()).containsExactly("two:name:Steve");
    }

    @Test
    void prerequisite_failure_sends_message_and_blocks_execution() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("secure") {{
            addPrerequisite(ctx -> {
                throw new CommandPrerequisiteException("blocked");
            });
            execute(ctx -> ctx.sendMessage("executed"));
        }});

        TestCommandContext ctx = runner.execute("secure");

        assertThat(ctx.messages()).containsExactly("blocked");
    }

    @Test
    void child_command_inherits_parent_prerequisite_by_default() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("root") {{
            addPrerequisite(ctx -> {
                throw new CommandPrerequisiteException("parent blocked");
            });
            addChildren(new TestCommand("child") {{
                execute(ctx -> ctx.sendMessage("executed"));
            }});
        }});

        TestCommandContext ctx = runner.execute("root child");

        assertThat(ctx.messages()).containsExactly("parent blocked");
    }

    @Test
    void child_command_can_disable_parent_prerequisite_inheritance() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("root") {{
            addPrerequisite(ctx -> {
                throw new CommandPrerequisiteException("parent blocked");
            });
            addChildren(new TestCommand("child") {{
                setInheritParentPrerequisite(false);
                execute(ctx -> ctx.sendMessage("executed"));
            }});
        }});

        TestCommandContext ctx = runner.execute("root child");

        assertThat(ctx.messages()).containsExactly("executed");
    }

    @Test
    void child_command_inherits_parent_preprocess_by_default() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("root") {{
            addPreprocess(ctx -> {
                ctx.sendMessage("parent preprocess");
                return true;
            });
            addChildren(new TestCommand("child") {{
                execute(ctx -> ctx.sendMessage("executed"));
            }});
        }});

        TestCommandContext ctx = runner.execute("root child");

        assertThat(ctx.messages()).containsExactly("parent preprocess", "executed");
    }

    @Test
    void child_command_can_disable_parent_preprocess_inheritance() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("root") {{
            addPreprocess(ctx -> {
                ctx.sendMessage("parent preprocess");
                return true;
            });
            addChildren(new TestCommand("child") {{
                setInheritParentPreprocess(false);
                execute(ctx -> ctx.sendMessage("executed"));
            }});
        }});

        TestCommandContext ctx = runner.execute("root child");

        assertThat(ctx.messages()).containsExactly("executed");
    }

    @Test
    void preprocess_can_block_execution_after_parsing() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("run") {{
            addPreprocess(ctx -> {
                ctx.sendMessage("preprocess:" + ctx.getInput("target"));
                return false;
            });
            argument(new CommonStringArgument<>("target"), (target, ctx) -> {
                ctx.sendMessage("executed:" + target);
            });
        }});

        TestCommandContext ctx = runner.execute("run Alex");

        assertThat(ctx.messages()).containsExactly("preprocess:Alex");
    }

    @Test
    void parent_help_includes_child_commands() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("game") {{
            description("Game command");
            addChildren(new TestCommand("start") {{
                description("Start game");
                execute(ctx -> ctx.sendMessage("started"));
            }}, new TestCommand("stop") {{
                description("Stop game");
                execute(ctx -> ctx.sendMessage("stopped"));
            }});
        }});

        TestCommandContext ctx = runner.execute("game");

        assertThat(ctx.messages()).anyMatch(x -> x.contains("Game command"))
                                  .anyMatch(x -> x.contains("Usage:"))
                                  .anyMatch(x -> x.contains("/game"))
                                  .anyMatch(x -> x.contains("start") && x.contains("Start game"))
                                  .anyMatch(x -> x.contains("stop") && x.contains("Stop game"));
    }

    @Test
    void execution_exception_is_reported_to_sender() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("boom") {{
            execute(ctx -> {
                throw new IllegalStateException("boom");
            });
        }});

        TestCommandContext ctx = runner.execute("boom");

        assertThat(ctx.messages()).containsExactly("An unexpected error occurred trying to execute that command.",
                                                   "Check the console for details.");
    }
}
