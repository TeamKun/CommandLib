package net.kunmc.lab.commandlib;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.stream.Collectors;

class Arguments {
    private final List<Argument<?>> argumentList;

    Arguments(List<Argument<?>> argumentList) {
        this.argumentList = argumentList;
    }

    void parse(List<Object> dst, CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        for (Argument<?> argument : argumentList) {
            try {
                dst.add(argument.parse(ctx));
            } catch (IllegalArgumentException ignored) {
                // 通常は発生しないが, argument追加時にContextActionを設定した場合やexecuteをOverrideした場合は
                // com.mojang.brigadier.context.CommandContext#getArgument内で例外が発生する可能性があるため
                // 例外を無視している.
            }
        }
    }

    String generateHelpMessage(String literalConcatName) {
        if (argumentList.isEmpty()) {
            return "";
        }

        String msg = TextFormatting.AQUA + "/" + literalConcatName + " ";
        msg += argumentList.stream()
                .map(Argument::generateHelpMessageTag)
                .collect(Collectors.joining(" "));

        return msg;
    }

    List<ArgumentCommandNode<CommandSource, ?>> toCommandNodes(Command parent) {
        return argumentList.stream()
                .map(a -> a.toBuilder(parent, this::parse))
                .map(RequiredArgumentBuilder::build)
                .collect(Collectors.toList());
    }
}
