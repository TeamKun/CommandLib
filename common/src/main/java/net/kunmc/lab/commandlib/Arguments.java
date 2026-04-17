package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.command.CommandHandler;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Arguments<C extends AbstractCommandContext<?, ?>> {
    private final List<? extends CommonArgument<?, C>> arguments;
    private final List<CommonCommand<C, ?, ?>> children;
    private String description = "";

    Arguments(List<? extends CommonArgument<?, C>> arguments, Collection<? extends CommonCommand<C, ?, ?>> children) {
        this.arguments = arguments;
        this.children = new ArrayList<>(children);
    }

    void parse(C ctx) throws ArgumentParseException {
        for (CommonArgument<?, C> argument : arguments) {
            // Help actions can run before the whole branch is typed; stop at the first missing token so partial
            // input still produces help instead of an unrelated parse failure.
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

    List<? extends CommonCommand<C, ?, ?>> children() {
        return List.copyOf(children);
    }

    void addChildren(Collection<? extends CommonCommand<C, ?, ?>> children) {
        this.children.addAll(children);
    }

    void contextAction(CommandHandler<C> contextAction) {
        if (arguments.isEmpty()) {
            return;
        }

        CommonArgument<?, C> last = arguments.get(arguments.size() - 1);
        last.contextAction(contextAction);
    }

    void description(String description) {
        this.description = description;
    }

    String description() {
        return description;
    }
}
