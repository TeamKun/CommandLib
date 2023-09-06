package net.kunmc.lab.commandlib;

import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.ChatColorUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Arguments<C extends AbstractCommandContext<?, ?>> {
    private final List<? extends CommonArgument<?, C>> arguments;

    Arguments(List<? extends CommonArgument<?, C>> arguments) {
        this.arguments = arguments;
    }

    void parse(C ctx) throws IncorrectArgumentInputException {
        long count = ctx.getHandle()
                        .getNodes()
                        .stream()
                        .map(ParsedCommandNode::getNode)
                        .filter(x -> x instanceof ArgumentCommandNode)
                        .count();

        for (int i = 0; i < count; i++) {
            CommonArgument<?, C> argument = arguments.get(i);
            Object parsedArg = argument.parse(ctx);
            ctx.addParsedArgument(argument.name(), parsedArg);
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
