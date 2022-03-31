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

import java.util.List;

public class LiteralArgument extends Argument<String> {
    private final List<String> literals;

    public LiteralArgument(String name, List<String> literals, ContextAction contextAction) {
        super(name, sb -> {
            List<String> inputs = sb.getArgs();
            String input = inputs.get(inputs.size() - 1);
           
            literals.stream()
                    .filter(s -> {
                        if (input.isEmpty()) {
                            return true;
                        }

                        return s.startsWith(input);
                    })
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.literals = literals;
    }

    @Override
    public String parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String arg = StringArgumentType.getString(ctx, name);
        return literals.stream()
                .filter(s -> s.equals(arg))
                .findFirst()
                .orElseThrow(() -> createException(ctx, arg));
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
