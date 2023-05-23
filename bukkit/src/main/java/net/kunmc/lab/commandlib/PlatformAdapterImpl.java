package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.chat.NMSChatMessage;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandSourceStack;
import net.kunmc.lab.commandlib.util.text.TextComponentBuilderImpl;
import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilderImpl;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

final class PlatformAdapterImpl implements PlatformAdapter<Object, Component, CommandContext, ArgumentBuilder, Command> {
    @Override
    public CommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<Object> ctx) {
        return new CommandContext(ctx, this);
    }

    @Override
    public ArgumentBuilder createArgumentBuilder() {
        return new ArgumentBuilder();
    }

    @Override
    public boolean hasPermission(Command command, Object commandSource) {
        return new NMSCommandSourceStack(commandSource).getBukkitSender()
                                                       .hasPermission(command.permissionName());
    }

    @Override
    public boolean hasPermission(Command command, CommandContext ctx) {
        return ctx.getSender()
                  .hasPermission(command.permissionName());
    }

    @Override
    public IncorrectArgumentInputException convertCommandSyntaxException(CommandSyntaxException e) {
        try {
            NMSChatMessage msg = new NMSChatMessage(e.getRawMessage());
            return new IncorrectArgumentInputException(ctx -> {
                ((CommandContext) ctx).sendFailure(Component.translatable(msg.getKey())
                                                            .args(Arrays.stream(msg.getArgs())
                                                                        .map(String::valueOf)
                                                                        .map(Component::text)
                                                                        .collect(Collectors.toList())));
            });

        } catch (Exception ex) {
            // TODO 1.19.4対応
        }

        throw new RuntimeException(String.format(
                "Cannot handle CommandSyntaxException. Because, Message's class(%s) is unsupported.",
                e.getRawMessage()
                 .getClass()));
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
