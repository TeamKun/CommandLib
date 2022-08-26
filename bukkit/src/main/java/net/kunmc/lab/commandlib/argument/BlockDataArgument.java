package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentTile;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BlockDataArgument extends Argument<BlockData> {
    private final Predicate<? super BlockData> filter;
    private final Function<? super BlockData, ? extends BlockData> shaper;

    public BlockDataArgument(String name, Consumer<Option> options) {
        super(name, ArgumentTile.a());

        Option option = new Option();
        options.accept(option);

        filter = option.filter;
        shaper = option.shaper;
        setSuggestionAction(option.suggestionAction);
        setContextAction(option.contextAction);
    }

    @Override
    public BlockData parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        System.out.println(getInputString(ctx, name));

        BlockData data = CraftBlockData.createData(ArgumentTile.a(ctx, name).a());
        if (filter != null && !filter.test(data)) {
            throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
        }

        if (shaper == null) {
            return data;
        }
        return shaper.apply(data);
    }

    @Accessors(chain = true, fluent = true)
    @Setter
    public static class Option {
        SuggestionAction suggestionAction;
        Predicate<? super BlockData> filter;
        Function<? super BlockData, ? extends BlockData> shaper;
        ContextAction contextAction;
    }
}