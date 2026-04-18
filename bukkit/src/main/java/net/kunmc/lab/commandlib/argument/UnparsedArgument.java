package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.suggestion.SuggestionAction;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentProfile;

import java.util.function.Supplier;

public class UnparsedArgument extends Argument<String, UnparsedArgument> {
    public UnparsedArgument(String name) {
        super(name,
              NMSArgumentProfile.create()
                                .argument());
        displayDefaultSuggestions(false);
        suggestionAction(((Supplier<SuggestionAction<CommandContext>>) () -> {
            if (suggestionAction() == null) {
                return sb -> {
                };
            }
            return suggestionAction();
        }).get());
    }

    @Override
    public String cast(Object parsedArgument) {
        return ((String) parsedArgument);
    }

    @Override
    protected String parseImpl(CommandContext ctx) throws ArgumentParseException {
        return ctx.getInput(name());
    }
}
