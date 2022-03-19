package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.BooleanArgument;
import net.kunmc.lab.commandlib.argument.DoubleArgument;
import net.kunmc.lab.commandlib.argument.FloatArgument;
import net.kunmc.lab.commandlib.argument.IntegerArgument;

import java.util.ArrayList;
import java.util.List;

public class ArgumentBuilder {
    private final List<Argument<?>> arguments = new ArrayList<>();

    public ArgumentBuilder boolArgument(String name) {
        return boolArgument(name, null);
    }

    public ArgumentBuilder boolArgument(String name, SuggestionAction suggestionAction) {
        return boolArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder boolArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new BooleanArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder doubleArgument(String name) {
        return doubleArgument(name, null);
    }

    public ArgumentBuilder doubleArgument(String name, Double min, Double max) {
        return doubleArgument(name, min, max, null);
    }

    public ArgumentBuilder doubleArgument(String name, SuggestionAction suggestionAction) {
        return doubleArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder doubleArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        return doubleArgument(name, -Double.MAX_VALUE, Double.MAX_VALUE, suggestionAction, contextAction);
    }

    public ArgumentBuilder doubleArgument(String name, Double min, Double max, SuggestionAction suggestionAction) {
        return doubleArgument(name, min, max, suggestionAction, null);
    }

    public ArgumentBuilder doubleArgument(String name, Double min, Double max, SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new DoubleArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    public ArgumentBuilder floatArgument(String name) {
        return floatArgument(name, null);
    }

    public ArgumentBuilder floatArgument(String name, Float min, Float max) {
        return floatArgument(name, min, max, null);
    }

    public ArgumentBuilder floatArgument(String name, SuggestionAction suggestionAction) {
        return floatArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder floatArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        return floatArgument(name, -Float.MAX_VALUE, Float.MAX_VALUE, suggestionAction, contextAction);
    }

    public ArgumentBuilder floatArgument(String name, Float min, Float max, SuggestionAction suggestionAction) {
        return floatArgument(name, min, max, suggestionAction, null);
    }

    public ArgumentBuilder floatArgument(String name, Float min, Float max, SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new FloatArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    public ArgumentBuilder integerArgument(String name) {
        return integerArgument(name, null);
    }

    public ArgumentBuilder integerArgument(String name, Integer min, Integer max) {
        return integerArgument(name, min, max, null);
    }

    public ArgumentBuilder integerArgument(String name, SuggestionAction suggestionAction) {
        return integerArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder integerArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        return integerArgument(name, Integer.MIN_VALUE, Integer.MAX_VALUE, suggestionAction, contextAction);
    }

    public ArgumentBuilder integerArgument(String name, Integer min, Integer max, SuggestionAction suggestionAction) {
        return integerArgument(name, min, max, suggestionAction, null);
    }

    public ArgumentBuilder integerArgument(String name, Integer min, Integer max, SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new IntegerArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    List<Argument<?>> build() {
        return arguments;
    }
}
