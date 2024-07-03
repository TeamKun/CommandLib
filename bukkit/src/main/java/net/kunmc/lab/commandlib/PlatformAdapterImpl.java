package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.chat.NMSChatMessage;
import net.kunmc.lab.commandlib.util.nms.chat.NMSIChatMutableComponent;
import net.kunmc.lab.commandlib.util.nms.chat.NMSTranslatableContents;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandListenerWrapper;
import net.kunmc.lab.commandlib.util.text.TextComponentBuilderImpl;
import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilderImpl;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public final class PlatformAdapterImpl implements PlatformAdapter<Object, BaseComponent, CommandContext, ArgumentBuilder, Command> {
    @Override
    public CommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<Object> ctx) {
        return new CommandContext(ctx);
    }

    @Override
    public ArgumentBuilder createArgumentBuilder() {
        return new ArgumentBuilder();
    }

    @Override
    public boolean hasPermission(Command command, Object commandSource) {
        return NMSCommandListenerWrapper.create(commandSource)
                                        .getBukkitSender()
                                        .hasPermission(command.permissionName());
    }

    @Override
    public boolean hasPermission(Command command, CommandContext ctx) {
        return ctx.getSender()
                  .hasPermission(command.permissionName());
    }

    @Override
    public IncorrectArgumentInputException convertCommandSyntaxException(CommandSyntaxException e) {
        if (NMSChatMessage.isSupportedVersion()) {
            NMSChatMessage msg = NMSChatMessage.create(e.getRawMessage());
            return new IncorrectArgumentInputException(ctx -> {
                TranslatableComponent component = new TranslatableComponent(msg.getKey(), msg.getArgs());
                ((CommandContext) ctx).sendFailure(component);
            });
        }

        NMSTranslatableContents contents = NMSIChatMutableComponent.create(e.getRawMessage())
                                                                   .getContentsAsTranslatable();
        return new IncorrectArgumentInputException(ctx -> {
            TranslatableComponent component = new TranslatableComponent(contents.getKey(), contents.getArgs());
            ((CommandContext) ctx).sendFailure(component);
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
