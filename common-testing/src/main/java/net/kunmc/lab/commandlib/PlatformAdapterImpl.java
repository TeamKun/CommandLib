package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.text.TextComponentBuilder;
import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilder;
import org.jetbrains.annotations.NotNull;

public final class PlatformAdapterImpl implements PlatformAdapter<FakeSender, String, TestCommandContext, TestCommand> {
    @Override
    public TestCommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<FakeSender> ctx) {
        return new TestCommandContext(ctx);
    }

    @Override
    public boolean hasPermission(TestCommand command, FakeSender commandSource, String permissionPrefix) {
        return hasPermission(commandSource, command.permissionName(permissionPrefix));
    }

    @Override
    public boolean hasPermission(TestCommand command, TestCommandContext ctx, String permissionPrefix) {
        return hasPermission(ctx, command.permissionName(permissionPrefix));
    }

    @Override
    public boolean hasPermission(FakeSender commandSource, String permissionNode) {
        return commandSource.hasPermission(permissionNode);
    }

    @Override
    public ArgumentParseException convertCommandSyntaxException(CommandSyntaxException e) {
        return new ArgumentParseException(ctx -> ctx.sendFailure(e.getMessage()));
    }

    @Override
    public TextComponentBuilder<String, ? extends String, ?> createTextComponentBuilder(@NotNull String text) {
        return new TestTextComponentBuilder(text);
    }

    @Override
    public TranslatableComponentBuilder<String, ? extends String, ?> createTranslatableComponentBuilder(@NotNull String key) {
        return new TestTranslatableComponentBuilder(key);
    }
}
