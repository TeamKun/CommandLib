package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.argument.CommonStringArgument;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommonCommandPermissionTest {
    @Test
    void argument_branch_permission_is_registered_with_generated_node_by_default() {
        TestCommand command = new TestCommand("config") {{
            argument(new CommonStringArgument<>("key")).description("Config key");
        }};

        List<PermissionConfig> configs = command.permissionConfigs("test.command");

        assertThat(configs).extracting(PermissionConfig::node)
                           .containsExactly("test.command.config", "test.command.config.key");
        PermissionConfig argumentConfig = configs.get(1);
        assertThat(argumentConfig.defaultPermission()).isEqualTo(DefaultPermission.OP);
        assertThat(argumentConfig.description()).isEmpty();
    }

    @Test
    void argument_branch_permission_can_change_default_and_description() {
        TestCommand command = new TestCommand("config") {{
            argument(new CommonStringArgument<>("key")).permission(DefaultPermission.ALL, "Config key");
        }};

        List<PermissionConfig> configs = command.permissionConfigs("test.command");

        assertThat(configs).extracting(PermissionConfig::node)
                           .containsExactly("test.command.config", "test.command.config.key");
        PermissionConfig argumentConfig = configs.get(1);
        assertThat(argumentConfig.defaultPermission()).isEqualTo(DefaultPermission.ALL);
        assertThat(argumentConfig.description()).isEqualTo("Config key");
    }

    @Test
    void argument_branch_permission_can_use_custom_node() {
        TestCommand command = new TestCommand("config") {{
            argument(new CommonStringArgument<>("key")).permission("custom.config.key", DefaultPermission.NONE);
        }};

        List<PermissionConfig> configs = command.permissionConfigs("test.command");

        assertThat(configs).extracting(PermissionConfig::node)
                           .contains("custom.config.key");
        assertThat(configs.get(1)
                          .defaultPermission()).isEqualTo(DefaultPermission.NONE);
    }

    @Test
    void child_command_inherits_parent_default_permission() {
        TestCommand command = new TestCommand("game") {{
            permission(DefaultPermission.ALL);
            addChildren(new TestCommand("start") {{
            }});
        }};

        List<PermissionConfig> configs = command.permissionConfigs("test.command");

        assertThat(configs).extracting(PermissionConfig::node)
                           .containsExactly("test.command.game", "test.command.game.start");
        assertThat(configs).extracting(PermissionConfig::defaultPermission)
                           .containsExactly(DefaultPermission.ALL, DefaultPermission.ALL);
    }

    @Test
    void child_command_can_override_inherited_default_permission() {
        TestCommand command = new TestCommand("game") {{
            permission(DefaultPermission.ALL);
            addChildren(new TestCommand("start") {{
                permission(DefaultPermission.NONE);
            }});
        }};

        List<PermissionConfig> configs = command.permissionConfigs("test.command");

        assertThat(configs).extracting(PermissionConfig::defaultPermission)
                           .containsExactly(DefaultPermission.ALL, DefaultPermission.NONE);
    }

    @Test
    void argument_branch_inherits_parent_command_default_permission() {
        TestCommand command = new TestCommand("config") {{
            permission(DefaultPermission.ALL);
            argument(new CommonStringArgument<>("key"));
        }};

        List<PermissionConfig> configs = command.permissionConfigs("test.command");

        assertThat(configs).extracting(PermissionConfig::node)
                           .containsExactly("test.command.config", "test.command.config.key");
        assertThat(configs).extracting(PermissionConfig::defaultPermission)
                           .containsExactly(DefaultPermission.ALL, DefaultPermission.ALL);
    }

    @Test
    void custom_argument_branch_node_inherits_parent_default_permission() {
        TestCommand command = new TestCommand("config") {{
            permission(DefaultPermission.ALL);
            argument(new CommonStringArgument<>("key")).permission("custom.config.key");
        }};

        List<PermissionConfig> configs = command.permissionConfigs("test.command");

        assertThat(configs.get(1)
                          .node()).isEqualTo("custom.config.key");
        assertThat(configs.get(1)
                          .defaultPermission()).isEqualTo(DefaultPermission.ALL);
    }

    @Test
    void argument_child_command_inherits_argument_branch_default_permission() {
        TestCommand command = new TestCommand("config") {{
            argument(new CommonStringArgument<>("key")).permission(DefaultPermission.ALL)
                                                       .child(new TestCommand("get") {{
                                                       }});
        }};

        List<PermissionConfig> configs = command.permissionConfigs("test.command");

        assertThat(configs).extracting(PermissionConfig::node)
                           .containsExactly("test.command.config",
                                            "test.command.config.key",
                                            "test.command.config.get");
        assertThat(configs).extracting(PermissionConfig::defaultPermission)
                           .containsExactly(DefaultPermission.OP, DefaultPermission.ALL, DefaultPermission.ALL);
    }

    @Test
    void argument_child_command_permission_blocks_execution_when_missing() {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("config") {{
            permission(DefaultPermission.ALL);
            argument(new CommonStringArgument<>("key")).permission(DefaultPermission.ALL)
                                                       .child(new TestCommand("get") {{
                                                           permission(DefaultPermission.NONE);
                                                           execute(ctx -> ctx.sendMessage("get"));
                                                       }});
        }}, new TestCommandSource("test.command.config", "test.command.config.key"));

        assertThatThrownBy(() -> runner.execute("config difficulty get")).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void argument_branch_default_permission_blocks_execution_when_generated_node_is_missing() {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("config") {{
            argument(new CommonStringArgument<>("key")).execute((key, ctx) -> ctx.sendMessage(key));
        }}, new TestCommandSource("test.command.config"));

        assertThatThrownBy(() -> runner.execute("config difficulty")).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void argument_branch_default_permission_allows_execution_when_generated_node_is_granted() {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("config") {{
            argument(new CommonStringArgument<>("key")).execute((key, ctx) -> ctx.sendMessage(key));
        }}, new TestCommandSource("test.command.config", "test.command.config.key"));

        TestCommandContext ctx = execute(runner, "config difficulty");

        assertThat(ctx.messages()).containsExactly("difficulty");
    }

    @Test
    void argument_branch_permission_blocks_execution_and_suggestions() {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("config") {{
            argument(suggestingStringArgument()).permission("custom.config.key")
                                                .execute((key, ctx) -> ctx.sendMessage(key));
        }}, new TestCommandSource("test.command.config"));

        assertThatThrownBy(() -> runner.execute("config difficulty")).isInstanceOf(CommandSyntaxException.class);
        assertThat(runner.suggest("config ")).containsExactly("help");
    }

    @Test
    void argument_branch_permission_allows_execution_and_suggestions_when_granted() {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("config") {{
            argument(suggestingStringArgument()).permission("custom.config.key")
                                                .execute((key, ctx) -> ctx.sendMessage(key));
        }}, new TestCommandSource("test.command.config", "custom.config.key"));

        TestCommandContext ctx = execute(runner, "config difficulty");

        assertThat(ctx.messages()).containsExactly("difficulty");
        assertThat(runner.suggest("config ")).containsExactlyInAnyOrder("difficulty", "help");
    }

    @Test
    void help_hides_argument_branch_without_permission() {
        TestCommandRunner runner = new TestCommandRunner(new TestCommand("config") {{
            argument(new CommonStringArgument<>("key")).permission("custom.config.key")
                                                       .description("Restricted key");
        }}, new TestCommandSource("test.command.config"));

        TestCommandContext ctx = execute(runner, "config");

        assertThat(ctx.messages()).noneMatch(x -> x.contains("Restricted key"));
    }

    private TestCommandContext execute(TestCommandRunner runner, String input) {
        try {
            return runner.execute(input);
        } catch (CommandSyntaxException e) {
            throw new AssertionError(e);
        }
    }

    private CommonArgument<String, TestCommandContext, ?> suggestingStringArgument() {
        return new StrArg("key").suggestionAction(sb -> sb.suggest("difficulty"));
    }

    private static final class StrArg extends CommonStringArgument<TestCommandContext, StrArg> {
        StrArg(String name) {
            super(name);
        }
    }
}
