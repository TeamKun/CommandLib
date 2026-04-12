package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.StringArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    }
}
