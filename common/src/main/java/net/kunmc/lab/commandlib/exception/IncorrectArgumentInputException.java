package net.kunmc.lab.commandlib.exception;

import com.mojang.brigadier.context.StringRange;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.PlatformAdapter;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.function.Consumer;

public final class IncorrectArgumentInputException extends Exception {
    private final Consumer<AbstractCommandContext<?, ?>> sendMessages;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <C extends AbstractCommandContext<?, ?>> IncorrectArgumentInputException(CommonArgument<?, C> argument,
                                                                                    C ctx,
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
        PlatformAdapter platformAdapter = PlatformAdapter.get();

        String str = input.substring(1, range.getStart());
        if (str.length() > 10) {
            str = "..." + str.substring(str.length() - 10);
        }

        String finalStr = str;
        this.sendMessages = context -> {
            AbstractCommandContext c = context;
            c.sendComponent(platformAdapter.createTranslatableComponentBuilder("command.unknown.argument")
                                           .color(ChatColorUtil.RED.getRGB())
                                           .build());
            c.sendComponent(platformAdapter.createTextComponentBuilder(ChatColorUtil.GRAY + finalStr + ChatColorUtil.RED + ChatColorUtil.UNDERLINE + incorrectInput + ChatColorUtil.RESET)
                                           .append(platformAdapter.createTranslatableComponentBuilder(
                                                                          "command.context.here")
                                                                  .italic()
                                                                  .color(ChatColorUtil.RED.getRGB())
                                                                  .build())
                                           .build());
        };
    }

    public IncorrectArgumentInputException(Consumer<AbstractCommandContext<?, ?>> sendMessages) {
        this.sendMessages = sendMessages;
    }

    public void sendMessage(AbstractCommandContext<?, ?> ctx) {
        sendMessages.accept(ctx);
    }
}
