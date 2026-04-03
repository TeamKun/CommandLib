package net.kunmc.lab.commandlib.exception;

import com.mojang.brigadier.context.StringRange;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.PlatformAdapter;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.Objects;
import java.util.function.Consumer;

public class ArgumentParseException extends Exception {
    private final Consumer<AbstractCommandContext<?, ?>> sendMessages;

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static <C extends AbstractCommandContext<?, ?>> Consumer<AbstractCommandContext<?, ?>> buildIncorrectInputMessage(
            String argumentName,
            C ctx,
            String incorrectInput) {
        String input = ctx.getHandle()
                          .getInput();
        StringRange range = ctx.getHandle()
                               .getNodes()
                               .stream()
                               .filter(n -> n.getNode()
                                             .getName()
                                             .equals(argumentName))
                               .findFirst()
                               .orElseThrow(IllegalStateException::new)
                               .getRange();
        PlatformAdapter platformAdapter = PlatformAdapter.get();

        String str = input.substring(1, range.getStart());
        if (str.length() > 10) {
            str = "..." + str.substring(str.length() - 10);
        }

        String finalStr = str;
        return context -> {
            AbstractCommandContext c = context;
            c.sendComponent(platformAdapter.createTranslatableComponentBuilder("command.unknown.argument")
                                           .color(Objects.requireNonNull(ChatColorUtil.RED.getRGB()))
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

    public static <C extends AbstractCommandContext<?, ?>> ArgumentParseException ofIncorrectInput(String argumentName,
                                                                                                   C ctx,
                                                                                                   String incorrectInput) {
        return new ArgumentParseException(buildIncorrectInputMessage(argumentName, ctx, incorrectInput));
    }

    public ArgumentParseException(String message, String... messages) {
        this(ctx -> {
            ctx.sendFailure(message);
            for (String s : messages) {
                ctx.sendFailure(s);
            }
        });
    }

    public ArgumentParseException(Consumer<AbstractCommandContext<?, ?>> sendMessages) {
        this.sendMessages = sendMessages;
    }

    public void sendMessage(AbstractCommandContext<?, ?> ctx) {
        sendMessages.accept(ctx);
    }
}
