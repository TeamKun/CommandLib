package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LiteralArgument extends Argument<String> {
    private final Supplier<Collection<String>> literalsSupplier;

    public LiteralArgument(String name, Collection<String> literals) {
        this(name, () -> literals, (Consumer<Option<String>>) option -> {
        });
    }

    public LiteralArgument(String name, Collection<String> literals, Consumer<Option<String>> options) {
        this(name, () -> literals, options);
    }

    public LiteralArgument(String name, Supplier<Collection<String>> literalsSupplier) {
        this(name, literalsSupplier, (Consumer<Option<String>>) option -> {
        });
    }

    public LiteralArgument(String name,
                           Supplier<Collection<String>> literalsSupplier,
                           Consumer<Option<String>> options) {
        super(name, StringArgumentType.string());
        this.literalsSupplier = literalsSupplier;

        setSuggestionAction(sb -> {
            literalsSupplier.get()
                            .stream()
                            .filter(x -> sb.getLatestInput()
                                           .isEmpty() || x.contains(sb.getLatestInput()))
                            .forEach(sb::suggest);
        });
        setOptions(options);
    }

    public LiteralArgument(String name, Supplier<Collection<String>> literalsSupplier, ContextAction contextAction) {
        super(name, sb -> {
            literalsSupplier.get()
                            .stream()
                            .filter(x -> sb.getLatestInput()
                                           .isEmpty() || x.contains(sb.getLatestInput()))
                            .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.literalsSupplier = literalsSupplier;
    }

    @Override
    public String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    public String parse(CommandContext ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name);
        return literalsSupplier.get()
                               .stream()
                               .filter(s::equals)
                               .findFirst()
                               .orElseThrow(() -> new IncorrectArgumentInputException(LiteralArgument.this, ctx, s));
    }
}
