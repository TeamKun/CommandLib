package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

public class StringArgument extends Argument<String> {
    public StringArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction, Type type) {
        super(name, suggestionAction, contextAction, type.type);
    }

    @Override
    public String parse(CommandContext<CommandListenerWrapper> ctx) {
        return StringArgumentType.getString(ctx, name);
    }

    public enum Type {
        WORD(StringArgumentType.word()),
        PHRASE_QUOTED(StringArgumentType.string()),
        PHRASE(StringArgumentType.greedyString());

        final StringArgumentType type;

        Type(StringArgumentType type) {
            this.type = type;
        }
    }
}
