package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.function.Predicate;

public class EnumArgument<T extends Enum<T>> extends Argument<T> {
    private final Class<T> clazz;
    private final Predicate<T> filter;

    public EnumArgument(String name, Class<T> clazz, Predicate<T> filter, ContextAction contextAction) {
        super(name, sb -> {
            Arrays.stream(clazz.getEnumConstants())
                    .filter(t -> {
                        if (filter != null) {
                            return filter.test(t);
                        }

                        return true;
                    })
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.clazz = clazz;
        this.filter = filter;
    }

    @Override
    public T parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return Arrays.stream(clazz.getEnumConstants())
                .filter(t -> {
                    if (filter != null) {
                        return filter.test(t);
                    }

                    return true;
                })
                .filter(x -> x.name().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> createException(ctx, s));
    }

    private IncorrectArgumentInputException createException(CommandContext<CommandListenerWrapper> ctx, String incorrectInput) {
        String input = ctx.getInput();
        Component unknownArgumentMsg = Component.translatable("command.unknown.argument", TextColor.color(ChatColor.RED.asBungee().getColor().getRGB()), Component.text(incorrectInput));

        StringRange range = ctx.getNodes().stream()
                .filter(n -> n.getNode().getName().equals(name))
                .findFirst()
                .get()
                .getRange();
        String s = input.substring(1, range.getStart());
        if (s.length() > 10) {
            s = "..." + s.substring(s.length() - 10);
        }
        Component hereMsg = Component.text(ChatColor.GRAY + s + ChatColor.RED + ChatColor.UNDERLINE + incorrectInput + ChatColor.RESET)
                .append(Component.translatable("command.context.here", Style.style().decorate(TextDecoration.ITALIC).color(TextColor.color(ChatColor.RED.asBungee().getColor().getRGB())).build()));

        return new IncorrectArgumentInputException(unknownArgumentMsg, hereMsg);
    }
}
