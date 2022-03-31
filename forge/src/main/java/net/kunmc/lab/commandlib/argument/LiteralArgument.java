package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class LiteralArgument extends Argument<String> {
    private final List<String> literals;

    public LiteralArgument(String name, List<String> literals, ContextAction contextAction) {
        super(name, sb -> literals.forEach(sb::suggest), contextAction, StringArgumentType.string());

        this.literals = literals;
    }

    @Override
    public String parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        String arg = StringArgumentType.getString(ctx, name);
        return literals.stream()
                .filter(s -> s.equals(arg))
                .findFirst()
                .orElseThrow(() -> createException(ctx, arg));
    }

    private IncorrectArgumentInputException createException(CommandContext<CommandSource> ctx, String incorrectInput) {
        String input = ctx.getInput();
        ITextComponent unknownArgumentMsg = new TranslationTextComponent("command.unknown.argument", incorrectInput).mergeStyle(TextFormatting.RED);

        StringRange range = ctx.getNodes().stream()
                .filter(n -> n.getNode().getName().equals(name))
                .findFirst()
                .get()
                .getRange();
        String s = input.substring(1, range.getStart());
        if (s.length() > 10) {
            s = "..." + s.substring(s.length() - 10);
        }
        ITextComponent hereMsg = new StringTextComponent(TextFormatting.GRAY + s + TextFormatting.RED + TextFormatting.UNDERLINE + incorrectInput + TextFormatting.RESET)
                .appendSibling(new TranslationTextComponent("command.context.here").mergeStyle(TextFormatting.ITALIC, TextFormatting.RED));

        return new IncorrectArgumentInputException(unknownArgumentMsg, hereMsg);
    }
}
