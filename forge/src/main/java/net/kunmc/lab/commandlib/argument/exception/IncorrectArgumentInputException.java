package net.kunmc.lab.commandlib.argument.exception;

import com.google.common.collect.Lists;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public final class IncorrectArgumentInputException extends Exception {
    private final List<ITextComponent> components;

    public static IncorrectArgumentInputException fromCommandSyntaxException(CommandSyntaxException e) {
        return new IncorrectArgumentInputException(((ITextComponent) e.getRawMessage()));
    }

    public IncorrectArgumentInputException(Argument<?> argument, CommandContext ctx, String incorrectInput) {
        String input = ctx.getHandle()
                          .getInput();
        ITextComponent unknownArgumentMsg = new TranslationTextComponent("command.unknown.argument",
                                                                         incorrectInput).mergeStyle(TextFormatting.RED);

        StringRange range = ctx.getHandle()
                               .getNodes()
                               .stream()
                               .filter(n -> n.getNode()
                                             .getName()
                                             .equals(argument.name()))
                               .findFirst()
                               .get()
                               .getRange();
        String s = input.substring(1, range.getStart());
        if (s.length() > 10) {
            s = "..." + s.substring(s.length() - 10);
        }
        ITextComponent hereMsg = new StringTextComponent(TextFormatting.GRAY + s + TextFormatting.RED + TextFormatting.UNDERLINE + incorrectInput + TextFormatting.RESET).appendSibling(
                new TranslationTextComponent("command.context.here").mergeStyle(TextFormatting.ITALIC,
                                                                                TextFormatting.RED));

        this.components = Lists.newArrayList(unknownArgumentMsg, hereMsg);
    }

    public IncorrectArgumentInputException(ITextComponent component, ITextComponent... components) {
        this.components = Lists.asList(component, components);
    }

    public void sendMessage(CommandContext context) {
        components.forEach(context::sendMessage);
    }
}
