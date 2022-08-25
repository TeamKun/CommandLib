package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.Collection;

public class LiteralArgument extends Argument<String> {
    private final Collection<String> literals;

    public LiteralArgument(String name, Collection<String> literals, ContextAction contextAction) {
        super(name, sb -> {
            literals.stream()
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.literals = literals;
    }

    @Override
    public String parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return literals.stream()
                .filter(s::equals)
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
