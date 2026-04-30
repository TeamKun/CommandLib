package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.StringArgument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandTesterTest {
    @Test
    void executesPaperCommandAndCapturesAdventureMessage() {
        Command command = new Command("hello") {{
            execute(ctx -> ctx.sendSuccess("Hello"));
        }};
        CommandTester tester = new CommandTester(command, "test.command");
        FakeSender sender = FakeSender.player("Steve");

        tester.execute("hello", sender);

        assertThat(sender.getSentMessageTexts()).containsExactly("Hello");
    }

    @Test
    void returnsSuggestionsWithTooltip() throws Exception {
        Command command = new Command("choose") {{
            argument(new StringArgument("value").suggestionAction(sb -> sb.suggest("alpha", "first value")))
                    .execute((value, ctx) -> ctx.sendSuccess(value));
        }};
        CommandTester tester = new CommandTester(command, "test.command");

        com.mojang.brigadier.suggestion.Suggestions suggestions = tester.suggestions("choose a", FakeSender.player(
                                                                                              "Steve"))
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
}
