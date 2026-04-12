package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.StringArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandTesterTest {
    @Nested
    class FakeSenderTest {
        @Test
        void player_is_instance_of_Player() {
            FakeSender sender = FakeSender.player("Steve");
            assertThat(sender.asSender()).isInstanceOf(Player.class);
        }

        @Test
        void console_is_instance_of_ConsoleCommandSender() {
            FakeSender sender = FakeSender.console();
            assertThat(sender.asSender()).isInstanceOf(ConsoleCommandSender.class);
        }

        @Test
        void player_name_is_set() {
            FakeSender sender = FakeSender.player("Steve");
            assertThat(((Player) sender.asSender()).getName()).isEqualTo("Steve");
        }

        @Test
        void player_locale_is_set() {
            FakeSender sender = FakeSender.player("Steve", "ja_jp");
            assertThat(((Player) sender.asSender()).getLocale()).isEqualTo("ja_jp");
        }

        @Test
        void sent_messages_are_empty_before_execution() {
            FakeSender sender = FakeSender.player("Steve");
            assertThat(sender.getSentMessages()).isEmpty();
        }
    }

    @Nested
    class Execute {
        @Test
        void message_is_captured() {
            try (CommandTester tester = new CommandTester(new Command("hello") {{
                execute(ctx -> ctx.sendMessage("Hello!"));
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve");
                tester.execute("hello", sender);
                assertThat(sender.getSentMessageTexts()).containsExactly("Hello!");
            }
        }

        @Test
        void send_success_has_green_color() {
            try (CommandTester tester = new CommandTester(new Command("hello") {{
                execute(ctx -> ctx.sendSuccess("done"));
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve");
                tester.execute("hello", sender);
                assertThat(sender.getSentMessages()).extracting(BaseComponent::getColor)
                                                    .containsExactly(ChatColor.GREEN);
            }
        }

        @Test
        void send_failure_has_red_color() {
            try (CommandTester tester = new CommandTester(new Command("hello") {{
                execute(ctx -> ctx.sendFailure("error"));
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve");
                tester.execute("hello", sender);
                assertThat(sender.getSentMessages()).extracting(BaseComponent::getColor)
                                                    .containsExactly(ChatColor.RED);
            }
        }

        @Test
        void multiple_messages_are_captured_in_order() {
            try (CommandTester tester = new CommandTester(new Command("hello") {{
                execute(ctx -> {
                    ctx.sendMessage("line1");
                    ctx.sendMessage("line2");
                    ctx.sendMessage("line3");
                });
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve");
                tester.execute("hello", sender);
                assertThat(sender.getSentMessageTexts()).containsExactly("line1", "line2", "line3");
            }
        }

        @Test
        void command_with_string_argument() {
            try (CommandTester tester = new CommandTester(new Command("greet") {{
                argument(new StringArgument("name"), (name, ctx) -> {
                    ctx.sendMessage("Hello, " + name + "!");
                });
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve");
                tester.execute("greet World", sender);
                assertThat(sender.getSentMessageTexts()).containsExactly("Hello, World!");
            }
        }

        @Test
        void command_context_returns_player_language() {
            try (CommandTester tester = new CommandTester(new Command("locale") {{
                execute(ctx -> ctx.sendMessage(ctx.getLanguage()));
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve", "ja_jp");
                tester.execute("locale", sender);
                assertThat(sender.getSentMessageTexts()).containsExactly("ja_jp");
            }
        }

        @Test
        void command_context_returns_player_locale() {
            try (CommandTester tester = new CommandTester(new Command("locale") {{
                execute(ctx -> ctx.sendMessage(ctx.getLocale()
                                                  .toLanguageTag()));
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve", "ja_jp");
                tester.execute("locale", sender);
                assertThat(sender.getSentMessageTexts()).containsExactly("ja-JP");
            }
        }

        @Test
        void require_player_passes_for_player_sender() {
            try (CommandTester tester = new CommandTester(new Command("playeronly") {{
                requirePlayer();
                execute(ctx -> ctx.sendMessage("ok"));
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve");
                tester.execute("playeronly", sender);
                assertThat(sender.getSentMessageTexts()).containsExactly("ok");
            }
        }

        @Test
        void require_player_blocks_console_sender() {
            try (CommandTester tester = new CommandTester(new Command("playeronly") {{
                requirePlayer();
                execute(ctx -> ctx.sendMessage("ok"));
            }}, "test.command")) {
                FakeSender sender = FakeSender.console();
                tester.execute("playeronly", sender);
                assertThat(sender.getSentMessageTexts()).doesNotContain("ok");
            }
        }

        @Test
        void require_console_passes_for_console_sender() {
            try (CommandTester tester = new CommandTester(new Command("consoleonly") {{
                requireConsole();
                execute(ctx -> ctx.sendMessage("ok"));
            }}, "test.command")) {
                FakeSender sender = FakeSender.console();
                tester.execute("consoleonly", sender);
                assertThat(sender.getSentMessageTexts()).containsExactly("ok");
            }
        }

        @Test
        void subcommand_message_is_captured() {
            try (CommandTester tester = new CommandTester(new Command("game") {{
                addChildren(new Command("start") {{
                    execute(ctx -> ctx.sendMessage("started"));
                }});
            }}, "test.command")) {
                FakeSender sender = FakeSender.player("Steve");
                tester.execute("game start", sender);
                assertThat(sender.getSentMessageTexts()).containsExactly("started");
            }
        }

        @Test
        void command_option_flag_is_available_from_context() {
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
        void command_option_flag_defaults_to_false() {
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
        void command_option_combined_short_flags_are_available_from_context() {
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
        void command_option_long_flag_is_available_from_context() {
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
        void command_value_option_is_available_from_context() {
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
        void command_value_option_long_name_is_available_from_context() {
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
        void command_value_option_defaults_when_omitted() {
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
        void command_string_value_option_defaults_when_omitted() {
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
        void command_string_value_option_without_value_is_rejected() {
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
        void command_option_after_argument_is_rejected() {
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
}
