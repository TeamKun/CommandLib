package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommonLiteralArgument<C extends AbstractCommandContext<?, ?>> extends CommonArgument<String, C> {
    private final Supplier<Collection<String>> literalsSupplier;

    public CommonLiteralArgument(String name, Collection<String> literals) {
        this(name, () -> literals, (Consumer<CommonArgument.Option<String, C>>) option -> {
        });
    }

    public CommonLiteralArgument(String name,
                                 Collection<String> literals,
                                 Consumer<CommonArgument.Option<String, C>> options) {
        this(name, () -> literals, options);
    }

    public CommonLiteralArgument(String name, Supplier<Collection<String>> literalsSupplier) {
        this(name, literalsSupplier, (Consumer<CommonArgument.Option<String, C>>) option -> {
        });
    }

    public CommonLiteralArgument(String name,
                                 Supplier<Collection<String>> literalsSupplier,
                                 Consumer<CommonArgument.Option<String, C>> options) {
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

    public CommonLiteralArgument(String name,
                                 Supplier<Collection<String>> literalsSupplier,
                                 ContextAction<C> contextAction) {
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
    public final String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    public final String parse(C ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx.getHandle(), name);
        return literalsSupplier.get()
                               .stream()
                               .filter(s::equals)
                               .findFirst()
                               .orElseThrow(() -> new IncorrectArgumentInputException(this, ctx, s));
    }
}
