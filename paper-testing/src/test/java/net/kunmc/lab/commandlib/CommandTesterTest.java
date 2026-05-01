package net.kunmc.lab.commandlib;

import com.mojang.brigadier.LiteralMessage;
import net.kunmc.lab.commandlib.argument.StringArgument;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandTesterTest {
    @Nested
    class FakeSenderTests {
        @Test
        void player_is_instance_of_Player() {
            assertThat(FakeSender.player("Steve")
                                 .asSender()).isInstanceOf(Player.class);
        }

        @Test
        void console_is_instance_of_ConsoleCommandSender() {
            assertThat(FakeSender.console()
                                 .asSender()).isInstanceOf(ConsoleCommandSender.class);
        }

        @Test
        void player_name_is_set() {
            assertThat(FakeSender.player("Steve")
                                 .asSender()
                                 .getName()).isEqualTo("Steve");
        }

        @Test
        void player_locale_is_set() {
            assertThat(((Player) FakeSender.player("Steve", "ja_jp")
                                           .asSender()).getLocale()).isEqualTo("ja_jp");
        }

        @Test
        void sent_messages_are_empty_before_execution() {
            assertThat(FakeSender.player("Steve")
                                 .getSentMessages()).isEmpty();
        }
    }

    @Nested
    class ExecuteTests {
        @Test
        void message_is_captured() {
            CommandTester tester = new CommandTester(new Command("hello") {{
                execute(ctx -> ctx.sendMessage("Hello!"));
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve");

            tester.execute("hello", sender);

            assertThat(sender.getSentMessageTexts()).containsExactly("Hello!");
        }

        @Test
        void send_success_has_green_color() {
            CommandTester tester = new CommandTester(new Command("hello") {{
                execute(ctx -> ctx.sendSuccess("done"));
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve");

            tester.execute("hello", sender);

            assertThat(sender.getSentMessages()).extracting(net.kyori.adventure.text.Component::color)
                                                .containsExactly(NamedTextColor.GREEN);
        }

        @Test
        void send_failure_has_red_color() {
            CommandTester tester = new CommandTester(new Command("hello") {{
                execute(ctx -> ctx.sendFailure("error"));
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve");

            tester.execute("hello", sender);

            assertThat(sender.getSentMessages()).extracting(net.kyori.adventure.text.Component::color)
                                                .containsExactly(NamedTextColor.RED);
        }

        @Test
        void multiple_messages_are_captured_in_order() {
            CommandTester tester = new CommandTester(new Command("hello") {{
                execute(ctx -> {
                    ctx.sendMessage("line1");
                    ctx.sendMessage("line2");
                    ctx.sendMessage("line3");
                });
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve");

            tester.execute("hello", sender);

            assertThat(sender.getSentMessageTexts()).containsExactly("line1", "line2", "line3");
        }

        @Test
        void command_with_string_argument() {
            CommandTester tester = new CommandTester(new Command("greet") {{
                argument(new StringArgument("name")).execute((name, ctx) -> ctx.sendMessage("Hello, " + name + "!"));
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve");

            tester.execute("greet World", sender);

            assertThat(sender.getSentMessageTexts()).containsExactly("Hello, World!");
        }

        @Test
        void command_context_returns_player_language() {
            CommandTester tester = new CommandTester(new Command("locale") {{
                execute(ctx -> ctx.sendMessage(ctx.getLanguage()));
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve", "ja_jp");

            tester.execute("locale", sender);

            assertThat(sender.getSentMessageTexts()).containsExactly("ja_jp");
        }

        @Test
        void command_context_returns_player_locale() {
            CommandTester tester = new CommandTester(new Command("locale") {{
                execute(ctx -> ctx.sendMessage(ctx.getLocale()
                                                  .toLanguageTag()));
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve", "ja_jp");

            tester.execute("locale", sender);

            assertThat(sender.getSentMessageTexts()).containsExactly("ja-JP");
        }

        @Test
        void subcommand_message_is_captured() {
            CommandTester tester = new CommandTester(new Command("game") {{
                addChildren(new Command("start") {{
                    execute(ctx -> ctx.sendMessage("started"));
                }});
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve");

            tester.execute("game start", sender);

            assertThat(sender.getSentMessageTexts()).containsExactly("started");
        }
    }

    @Nested
    class SuggestionsTests {
        @Test
        void returns_suggestions_with_string_tooltip() throws Exception {
            CommandTester tester = new CommandTester(new Command("choose") {{
                argument(new StringArgument("value").suggestionAction(sb -> sb.suggest("alpha",
                                                                                       "first value"))).execute((value, ctx) -> ctx.sendSuccess(
                        value));
            }}, "test.command");

            var suggestions = tester.suggestions("choose a", FakeSender.player("Steve"))
                                    .get();

            assertThat(suggestions.getList()).hasSize(1);
            assertThat(suggestions.getList()
                                  .get(0)
                                  .getText()).isEqualTo("alpha");
            assertThat(suggestions.getList()
                                  .get(0)
                                  .getTooltip()
                                  .getString()).isEqualTo("first value");
        }

        @Test
        void returns_suggestions_with_message_tooltip() throws Exception {
            CommandTester tester = new CommandTester(new Command("cmd") {{
                argument(new StringArgument("value").suggestionAction(sb -> sb.suggest("alpha",
                                                                                       new LiteralMessage(
                                                                                               "alpha tooltip"))
                                                                              .suggest("beta"))).execute((v, ctx) -> {
                });
            }}, "test.command");
            FakeSender sender = FakeSender.player("Steve");

            var suggestions = tester.suggestions("cmd ", sender)
                                    .get()
                                    .getList();

            assertThat(suggestions).extracting(com.mojang.brigadier.suggestion.Suggestion::getText)
                                   .containsExactly("alpha", "beta", "help");
            assertThat(suggestions.get(0)
                                  .getTooltip()
                                  .getString()).isEqualTo("alpha tooltip");
            assertThat(suggestions.get(1)
                                  .getTooltip()).isNull();
        }
    }

    @Nested
    class BuilderTests {
        @Test
        void command_can_be_registered_with_builder() {
            CommandTester tester = CommandTester.builder()
                                                .command(new Command("hello") {{
                                                    execute(ctx -> ctx.sendMessage("Hello!"));
                                                }})
                                                .permissionPrefix("test.command")
                                                .build();
            FakeSender sender = FakeSender.player("Steve");

            tester.execute("hello", sender);

            assertThat(sender.getSentMessageTexts()).containsExactly("Hello!");
        }
    }
}
