package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.CommonArgument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.StringUtil;

import java.util.Collection;
import java.util.Objects;
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
        this.literalsSupplier = Objects.requireNonNull(literalsSupplier);

        suggestionAction(sb -> {
            literalsSupplier.get()
                            .stream()
                            .filter(x -> sb.getLatestInput()
                                           .isEmpty() || StringUtil.containsIgnoreCase(x, sb.getLatestInput()))
                            .forEach(sb::suggest);
        });
        applyOptions(options);
    }

    public CommonLiteralArgument(String name,
                                 Supplier<Collection<String>> literalsSupplier,
                                 ContextAction<C> contextAction) {
        super(name, sb -> {
            literalsSupplier.get()
                            .stream()
                            .filter(x -> sb.getLatestInput()
                                           .isEmpty() || StringUtil.containsIgnoreCase(x, sb.getLatestInput()))
                            .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.literalsSupplier = literalsSupplier;
    }

    @Override
    public final String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    protected final String parseImpl(C ctx) throws ArgumentParseException {
        String s = StringArgumentType.getString(ctx.getHandle(), name());
        return literalsSupplier.get()
                               .stream()
                               .filter(s::equals)
                               .findFirst()
                               .orElseThrow(() -> ArgumentParseException.ofIncorrectInput(this.name(), ctx, s));
    }
}
