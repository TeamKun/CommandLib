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

import java.util.Arrays;
import java.util.function.Predicate;

public class EnumArgument<T extends Enum<T>> extends Argument<T> {
    private final Class<T> clazz;
    private final Predicate<T> filter;

    public EnumArgument(String name, Class<T> clazz, Predicate<T> filter, ContextAction contextAction) {
        super(name, sb -> {
            Arrays.stream(clazz.getEnumConstants())
                    .filter(x -> filter == null || filter.test(x))
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .filter(x -> sb.getLatestInput().isEmpty() || x.contains(sb.getLatestInput()))
                    .forEach(sb::suggest);
        }, contextAction, StringArgumentType.string());

        this.clazz = clazz;
        this.filter = filter;
    }

    @Override
    public T parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException {
        String s = StringArgumentType.getString(ctx, name);
        return Arrays.stream(clazz.getEnumConstants())
                .filter(x -> filter == null || filter.test(x))
                .filter(x -> x.name().equalsIgnoreCase(s))
                .findFirst()
                .orElseThrow(() -> createException(ctx, s));
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
