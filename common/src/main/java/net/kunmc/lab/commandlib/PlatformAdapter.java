package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.text.TextComponentBuilder;
import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilder;
import org.jetbrains.annotations.NotNull;

public interface PlatformAdapter<S, T, C extends AbstractCommandContext<S, T>, B extends AbstractArgumentBuilder<C, B>, U extends CommonCommand<C, B, U>> {
    C createCommandContext(com.mojang.brigadier.context.CommandContext<S> ctx);

    B createArgumentBuilder();

    boolean hasPermission(U command, S commandSource);

    boolean hasPermission(U command, C ctx);

    IncorrectArgumentInputException convertCommandSyntaxException(CommandSyntaxException e);

    <A extends TextComponentBuilder<T, ? extends T, A>> A createTextComponentBuilder(@NotNull String text);

    <A extends TranslatableComponentBuilder<T, ? extends T, A>> A createTranslatableComponentBuilder(@NotNull String key);
}
