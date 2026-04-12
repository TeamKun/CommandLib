package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Arguments<C extends AbstractCommandContext<?, ?>> {
    private final List<? extends CommonArgument<?, C>> arguments;

    Arguments(List<? extends CommonArgument<?, C>> arguments) {
        this.arguments = arguments;
    }

    void parse(C ctx) throws ArgumentParseException {
        for (CommonArgument<?, C> argument : arguments) {
            if (!ctx.hasInput(argument.name())) {
                return;
            }
            Object parsedArg = argument.parse(ctx);
            ctx.setParsedArgument(argument.name(), parsedArg);
        }
    }

    String concatTagNames() {
        if (arguments.isEmpty()) {
            return "";
        }

        return arguments.stream()
                        .map(x -> String.format(ChatColorUtil.GRAY + "<" + ChatColorUtil.YELLOW + "%s" + ChatColorUtil.GRAY + ">",
                                                x.name()))
                        .collect(Collectors.joining(" "));
    }

    int size() {
        return arguments.size();
    }

    Stream<? extends CommonArgument<?, C>> stream() {
        return arguments.stream();
    }
}
