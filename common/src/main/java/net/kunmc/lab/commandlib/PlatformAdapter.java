package net.kunmc.lab.commandlib;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.text.TextComponentBuilder;
import net.kunmc.lab.commandlib.util.text.TranslatableComponentBuilder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public interface PlatformAdapter<S, T, C extends AbstractCommandContext<S, T>, B extends AbstractArgumentBuilder<C, B>, U extends CommonCommand<C, B, U>> {
    @SuppressWarnings("unchecked")
    static <S, T, C extends AbstractCommandContext<S, T>, B extends AbstractArgumentBuilder<C, B>, U extends CommonCommand<C, B, U>> PlatformAdapter<S, T, C, B, U> get() {
        try {
            Class<PlatformAdapter<S, T, C, B, U>> platformAdapterImplClass = (Class<PlatformAdapter<S, T, C, B, U>>) Class.forName(
                    "net.kunmc.lab.commandlib.PlatformAdapterImpl");
            return platformAdapterImplClass.getConstructor()
                                           .newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Could not find PlatformAdapter", e);
        }
    }

    C createCommandContext(com.mojang.brigadier.context.CommandContext<S> ctx);

    B createArgumentBuilder();

    boolean hasPermission(U command, S commandSource);

    boolean hasPermission(U command, C ctx);

    IncorrectArgumentInputException convertCommandSyntaxException(CommandSyntaxException e);

    TextComponentBuilder<T, ? extends T, ?> createTextComponentBuilder(@NotNull String text);

    TranslatableComponentBuilder<T, ? extends T, ?> createTranslatableComponentBuilder(@NotNull String key);
}
