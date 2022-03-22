package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

public class BooleanArgument extends Argument<Boolean> {
    public BooleanArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, BoolArgumentType.bool());
    }

    @Override
    public Boolean parse(CommandContext<CommandListenerWrapper> ctx) {
        return BoolArgumentType.getBool(ctx, name);
    }
}
