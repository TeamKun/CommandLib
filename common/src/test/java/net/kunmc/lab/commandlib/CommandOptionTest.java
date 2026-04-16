package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.CommonStringArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandOptionTest {
    @Test
    void flag_is_available_from_context() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Boolean, TestCommandContext> force = option(Options.flag("force", 'f'));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force));
            });
        }});

        TestCommandContext ctx = runner.execute("scan -f Alex");

        assertThat(ctx.messages()).containsExactly("Alex:true");
    }

    @Test
    void flag_defaults_to_false() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Boolean, TestCommandContext> force = option(Options.flag("force", 'f'));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force));
            });
        }});

        TestCommandContext ctx = runner.execute("scan Alex");

        assertThat(ctx.messages()).containsExactly("Alex:false");
    }

    @Test
    void combined_short_flags_are_available_from_context() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Boolean, TestCommandContext> force = option(Options.flag("force", 'f'));
            CommandOption<Boolean, TestCommandContext> verbose = option(Options.flag("verbose", 'v'));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force) + ":" + ctx.getOption(verbose));
            });
        }});

        TestCommandContext ctx = runner.execute("scan -fv Alex");

        assertThat(ctx.messages()).containsExactly("Alex:true:true");
    }

    @Test
    void long_flag_is_available_from_context() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Boolean, TestCommandContext> force = option(Options.flag("force", 'f'));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force));
            });
        }});

        TestCommandContext ctx = runner.execute("scan --force Alex");

        assertThat(ctx.messages()).containsExactly("Alex:true");
    }

    @Test
    void value_option_is_available_from_context() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Integer, TestCommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(limit));
            });
        }});

        TestCommandContext ctx = runner.execute("scan -n 20 Alex");

        assertThat(ctx.messages()).containsExactly("Alex:20");
    }

    @Test
    void value_option_long_name_is_available_from_context() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Integer, TestCommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(limit));
            });
        }});

        TestCommandContext ctx = runner.execute("scan --limit 20 Alex");

        assertThat(ctx.messages()).containsExactly("Alex:20");
    }

    @Test
    void value_option_defaults_when_omitted() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Integer, TestCommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(limit));
            });
        }});

        TestCommandContext ctx = runner.execute("scan Alex");

        assertThat(ctx.messages()).containsExactly("Alex:10");
    }

    @Test
    void string_value_option_defaults_when_omitted() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<String, TestCommandContext> format = option(Options.string("format", 'F', "text"));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(format));
            });
        }});

        TestCommandContext ctx = runner.execute("scan Alex");

        assertThat(ctx.messages()).containsExactly("Alex:text");
    }

    @Test
    void string_value_option_without_value_is_rejected() {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<String, TestCommandContext> format = option(Options.string("format", 'F', "text"));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(format));
            });
        }});

        assertThatThrownBy(() -> runner.execute("scan --format")).isInstanceOf(Exception.class);
    }

    @Test
    void context_reports_whether_option_is_present() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Boolean, TestCommandContext> force = option(Options.flag("force", 'f'));
            CommandOption<Integer, TestCommandContext> limit = option(Options.integer("limit", 'n', 10));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(ctx.hasOption(force) + ":" + ctx.hasOption(limit) + ":" + ctx.getOption(limit));
            });
        }});

        TestCommandContext ctx = runner.execute("scan -n 10 Alex");

        assertThat(ctx.messages()).containsExactly("false:true:10");
    }

    @Test
    void option_can_require_another_option() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Boolean, TestCommandContext> force = option(Options.flag("force", 'f'));
            CommandOption<String, TestCommandContext> reason = option(Options.string("reason", 'r', "")
                                                                             .requires(force));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force) + ":" + ctx.getOption(reason));
            });
        }});

        TestCommandContext failed = runner.execute("scan -r cleanup Alex");
        TestCommandContext passed = runner.execute("scan -f -r cleanup Alex");

        assertThat(failed.messages()).anyMatch(x -> x.contains("--reason requires --force"));
        assertThat(passed.messages()).containsExactly("Alex:true:cleanup");
    }

    @Test
    void option_can_require_another_option_value() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<String, TestCommandContext> mode = option(Options.string("mode", 'm', "normal"));
            CommandOption<Integer, TestCommandContext> limit = option(Options.integer("limit", 'n', 10)
                                                                             .requires(mode, "parallel"));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(mode) + ":" + ctx.getOption(limit));
            });
        }});

        TestCommandContext failed = runner.execute("scan -m normal -n 20 Alex");
        TestCommandContext passed = runner.execute("scan -n 20 -m parallel Alex");

        assertThat(failed.messages()).anyMatch(x -> x.contains("--limit requires --mode to be parallel"));
        assertThat(passed.messages()).containsExactly("Alex:parallel:20");
    }

    @Test
    void option_value_requirement_uses_default_value() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<String, TestCommandContext> mode = option(Options.string("mode", 'm', "parallel"));
            CommandOption<Integer, TestCommandContext> limit = option(Options.integer("limit", 'n', 10)
                                                                             .requires(mode, "parallel"));
            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(mode) + ":" + ctx.getOption(limit));
            });
        }});

        TestCommandContext ctx = runner.execute("scan -n 20 Alex");

        assertThat(ctx.messages()).containsExactly("Alex:parallel:20");
    }

    @Test
    void option_after_argument_is_rejected() {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            CommandOption<Boolean, TestCommandContext> force = option(Options.flag("force", 'f'));
            argument(new CommonStringArgument<>("target", CommonStringArgument.Type.WORD)).execute((target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force));
            });
        }});

        assertThatThrownBy(() -> runner.execute("scan Alex -f")).isInstanceOf(Exception.class);
    }

    @Test
    void child_command_uses_child_options_after_child_name() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("game") {{
            addChildren(new TestCommand("start") {{
                CommandOption<Boolean, TestCommandContext> force = option(Options.flag("force", 'f'));
                argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
                    ctx.sendMessage(target + ":" + ctx.getOption(force));
                });
            }});
        }});

        TestCommandContext ctx = runner.execute("game start -f Alex");

        assertThat(ctx.messages()).containsExactly("Alex:true");
    }

    @Test
    void help_message_includes_options() throws Exception {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("scan") {{
            option(Options.flag("force", 'f')
                          .description("Force execution"));
            option(Options.integer("limit", 'n', 10, 1, 100)
                          .description("Maximum count"));

            argument(new CommonStringArgument<>("target")).execute((target, ctx) -> {
            });
        }});

        TestCommandContext ctx = runner.execute("scan");

        assertThat(ctx.messages()).anyMatch(x -> x.contains("Usage:"))
                                  .anyMatch(x -> x.contains("/scan") && x.contains("options") && x.contains("target"))
                                  .anyMatch(x -> x.contains("Options:"))
                                  .anyMatch(x -> x.contains("-f") && x.contains("--force") && x.contains("Force execution"))
                                  .anyMatch(x -> x.contains("-n") && x.contains("--limit") && x.contains("Maximum count"));
    }
}
