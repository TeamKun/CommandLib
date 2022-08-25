package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.function.Supplier;

public class LiteralArgument extends Argument<String> {
    private final Supplier<Collection<String>> literalsSupplier;

    public LiteralArgument(String name, Supplier<Collection<String>> literalsSupplier, ContextAction contextAction) {
        super(name, sb -> {
            literalsSupplier.get().stream()
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.literalsSupplier = literalsSupplier;
    }

    @Override
    public String parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return literalsSupplier.get().stream()
                .filter(s::equals)
                .findFirst()
                .orElseThrow(() -> new IncorrectArgumentInputException(LiteralArgument.this, ctx, s));
    }
}
