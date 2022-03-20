package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.*;

import java.util.ArrayList;
import java.util.List;

public class ArgumentBuilder {
    private final List<Argument<?>> arguments = new ArrayList<>();

    public ArgumentBuilder blockPosArgument(String name) {
        return blockPosArgument(name, null);
    }

    public ArgumentBuilder blockPosArgument(String name, SuggestionAction suggestionAction) {
        return blockPosArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder blockPosArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new BlockPosArgument(name, suggestionAction, contextAction));
        return this;
    }

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

    public ArgumentBuilder entityArgument(String name) {
        return entityArgument(name, null);
    }

    public ArgumentBuilder entityArgument(String name, boolean enableEntities, boolean single) {
        return entityArgument(name, enableEntities, single, null);
    }

    public ArgumentBuilder entityArgument(String name, SuggestionAction suggestionAction) {
        return entityArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder entityArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        return entityArgument(name, true, false, suggestionAction, contextAction);
    }

    public ArgumentBuilder entityArgument(String name, boolean enableEntities, boolean single, SuggestionAction suggestionAction) {
        return entityArgument(name, enableEntities, single, suggestionAction, null);
    }

    public ArgumentBuilder entityArgument(String name, boolean enableEntities, boolean single, SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new EntityArgument(name, suggestionAction, contextAction, enableEntities, single));
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
