package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.StringArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandOptionTest {
    @Test
    void flag_is_available_from_context() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan -f Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:true");
        }
    }

    @Test
    void flag_defaults_to_false() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:false");
        }
    }

    @Test
    void combined_short_flags_are_available_from_context() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            CommandOption<Boolean, CommandContext> verbose = option(Options.flag("verbose", 'v'));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force) + ":" + ctx.getOption(verbose));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan -fv Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:true:true");
        }
    }

    @Test
    void long_flag_is_available_from_context() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan --force Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:true");
        }
    }

    @Test
    void value_option_is_available_from_context() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(limit));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan -n 20 Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:20");
        }
    }

    @Test
    void value_option_long_name_is_available_from_context() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(limit));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan --limit 20 Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:20");
        }
    }

    @Test
    void value_option_defaults_when_omitted() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10, 1, 100));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(limit));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:10");
        }
    }

    @Test
    void string_value_option_defaults_when_omitted() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<String, CommandContext> format = option(Options.string("format", 'F', "text"));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(format));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:text");
        }
    }

    @Test
    void string_value_option_without_value_is_rejected() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<String, CommandContext> format = option(Options.string("format", 'F', "text"));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(format));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            assertThatThrownBy(() -> tester.execute("scan --format", sender)).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    void context_reports_whether_option_is_present() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(ctx.hasOption(force) + ":" + ctx.hasOption(limit) + ":" + ctx.getOption(limit));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan -n 10 Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("false:true:10");
        }
    }

    @Test
    void option_can_require_another_option() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            CommandOption<String, CommandContext> reason = option(Options.string("reason", 'r', "")
                                                                         .requires(force));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force) + ":" + ctx.getOption(reason));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan -r cleanup Alex", sender);
            assertThat(sender.getSentMessageTexts()).anyMatch(x -> x.contains("--reason requires --force"));

            FakeSender sender2 = FakeSender.player("Alex");
            tester.execute("scan -f -r cleanup Alex", sender2);
            assertThat(sender2.getSentMessageTexts()).containsExactly("Alex:true:cleanup");
        }
    }

    @Test
    void option_can_require_another_option_value() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<String, CommandContext> mode = option(Options.string("mode", 'm', "normal"));
            CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10)
                                                                         .requires(mode, "parallel"));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(mode) + ":" + ctx.getOption(limit));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan -m normal -n 20 Alex", sender);
            assertThat(sender.getSentMessageTexts()).anyMatch(x -> x.contains("--limit requires --mode to be parallel"));

            FakeSender sender2 = FakeSender.player("Alex");
            tester.execute("scan -n 20 -m parallel Alex", sender2);
            assertThat(sender2.getSentMessageTexts()).containsExactly("Alex:parallel:20");
        }
    }

    @Test
    void option_value_requirement_uses_default_value() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<String, CommandContext> mode = option(Options.string("mode", 'm', "parallel"));
            CommandOption<Integer, CommandContext> limit = option(Options.integer("limit", 'n', 10)
                                                                         .requires(mode, "parallel"));
            argument(new StringArgument("target"), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(mode) + ":" + ctx.getOption(limit));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan -n 20 Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:parallel:20");
        }
    }

    @Test
    void option_after_argument_is_rejected() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
            argument(new StringArgument("target", StringArgument.Type.WORD), (target, ctx) -> {
                ctx.sendMessage(target + ":" + ctx.getOption(force));
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            assertThatThrownBy(() -> tester.execute("scan Alex -f", sender)).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    void child_command_uses_child_options_after_child_name() {
        try (CommandTester tester = new CommandTester(new Command("game") {{
            addChildren(new Command("start") {{
                CommandOption<Boolean, CommandContext> force = option(Options.flag("force", 'f'));
                argument(new StringArgument("target"), (target, ctx) -> {
                    ctx.sendMessage(target + ":" + ctx.getOption(force));
                });
            }});
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("game start -f Alex", sender);
            assertThat(sender.getSentMessageTexts()).containsExactly("Alex:true");
        }
    }

    @Test
    void help_message_includes_options() {
        try (CommandTester tester = new CommandTester(new Command("scan") {{
            option(Options.flag("force", 'f')
                          .description("Force execution"));
            option(Options.integer("limit", 'n', 10, 1, 100)
                          .description("Maximum count"));

            argument(new StringArgument("target"), (target, ctx) -> {
            });
        }}, "test.command")) {
            FakeSender sender = FakeSender.player("Steve");
            tester.execute("scan", sender);
            assertThat(sender.getSentMessageTexts()).anyMatch(x -> x.contains("Usage:"))
                                                    .anyMatch(x -> x.contains("/scan") && x.contains("options") && x.contains(
                                                            "target"))
                                                    .anyMatch(x -> x.contains("Options:"))
                                                    .anyMatch(x -> x.contains("-f") && x.contains("--force") && x.contains(
                                                            "Force execution"))
                                                    .anyMatch(x -> x.contains("-n") && x.contains("--limit") && x.contains(
                                                            "Maximum count"));
        }
    }
}
