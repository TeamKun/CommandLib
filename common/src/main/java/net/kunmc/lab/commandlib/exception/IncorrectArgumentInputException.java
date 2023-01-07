package net.kunmc.lab.commandlib.exception;

import com.mojang.brigadier.context.StringRange;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.function.Consumer;

public final class IncorrectArgumentInputException extends Exception {
    private final Consumer<AbstractCommandContext<?, ?>> sendMessages;

    public IncorrectArgumentInputException(CommonArgument<?, ?> argument,
                                           AbstractCommandContext<?, ?> ctx,
                                           String incorrectInput) {
        String input = ctx.getHandle()
                          .getInput();
        StringRange range = ctx.getHandle()
                               .getNodes()
                               .stream()
                               .filter(n -> n.getNode()
                                             .getName()
                                             .equals(argument.name()))
                               .findFirst()
                               .get()
                               .getRange();
        String s = input.substring(1, range.getStart());
        if (s.length() > 10) {
            s = "..." + s.substring(s.length() - 10);
        }

        this.sendMessages = context -> {
            String str = input.substring(1, range.getStart());
            if (str.length() > 10) {
                str = "..." + str.substring(str.length() - 10);
            }
            ctx.sendRawComponentBuilder((ctx.createTranslatableComponentBuilder("command.unknown.argument")
                                            .color(ChatColorUtil.RED.getRGB())));
            ctx.sendRawComponentBuilder(ctx.createTextComponentBuilder(ChatColorUtil.GRAY + str + ChatColorUtil.RED + ChatColorUtil.UNDERLINE + incorrectInput + ChatColorUtil.RESET)
                                           .append(ctx.createTranslatableComponentBuilder("command.context.here")
                                                      .italic()
                                                      .color(ChatColorUtil.RED.getRGB())));
        };
    }

    public IncorrectArgumentInputException(Consumer<AbstractCommandContext<?, ?>> sendMessages) {
        this.sendMessages = sendMessages;
    }

    public void sendMessage(AbstractCommandContext<?, ?> ctx) {
        sendMessages.accept(ctx);
    }
}
