package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.text.TextComponentBuilderImpl;
import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilderImpl;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

public final class PlatformAdapterImpl implements PlatformAdapter<CommandSource, ITextComponent, CommandContext, ArgumentBuilder, Command> {
    @Override
    public CommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<CommandSource> ctx) {
        return new CommandContext(ctx);
    }

    @Override
    public ArgumentBuilder createArgumentBuilder() {
        return new ArgumentBuilder();
    }

    @Override
    public boolean hasPermission(Command command, CommandSource commandSource) {
        return commandSource.hasPermissionLevel(command.permissionLevel());
    }

    @Override
    public boolean hasPermission(Command command, CommandContext ctx) {
        return ctx.getSender()
                  .hasPermissionLevel(command.permissionLevel());
    }

    @Override
    public IncorrectArgumentInputException convertCommandSyntaxException(CommandSyntaxException e) {
        return new IncorrectArgumentInputException(ctx -> {
            ((CommandContext) ctx).sendMessage(((ITextComponent) e.getRawMessage()));
        });
    }

    @Override
    public TextComponentBuilderImpl createTextComponentBuilder(@NotNull String text) {
        return new TextComponentBuilderImpl(text);
    }

    @Override
    public TranslatableComponentBuilderImpl createTranslatableComponentBuilder(@NotNull String key) {
        return new TranslatableComponentBuilderImpl(key);
    }
}
