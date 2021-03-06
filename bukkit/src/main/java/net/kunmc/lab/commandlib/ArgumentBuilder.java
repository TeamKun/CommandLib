package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ArgumentBuilder {
    private final List<Argument<?>> arguments = new ArrayList<>();
    private ContextAction contextAction = null;

    public ArgumentBuilder blockDataArgument(@NotNull String name) {
        return blockDataArgument(name, null);
    }

    public ArgumentBuilder blockDataArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return blockDataArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder blockDataArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new BlockDataArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder boolArgument(@NotNull String name) {
        return boolArgument(name, null);
    }

    public ArgumentBuilder boolArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return boolArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder boolArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new BooleanArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder doubleArgument(@NotNull String name) {
        return doubleArgument(name, null);
    }

    public ArgumentBuilder doubleArgument(@NotNull String name, Double min, Double max) {
        return doubleArgument(name, min, max, null);
    }

    public ArgumentBuilder doubleArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return doubleArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder doubleArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return doubleArgument(name, -Double.MAX_VALUE, Double.MAX_VALUE, suggestionAction, contextAction);
    }

    public ArgumentBuilder doubleArgument(@NotNull String name, Double min, Double max, @Nullable SuggestionAction suggestionAction) {
        return doubleArgument(name, min, max, suggestionAction, null);
    }

    public ArgumentBuilder doubleArgument(@NotNull String name, Double min, Double max, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new DoubleArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    public ArgumentBuilder enchantmentArgument(@NotNull String name) {
        return enchantmentArgument(name, null);
    }

    public ArgumentBuilder enchantmentArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return enchantmentArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder enchantmentArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new EnchantmentArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder entityArgument(@NotNull String name) {
        return entityArgument(name, null);
    }

    public ArgumentBuilder entityArgument(@NotNull String name, boolean enableEntities, boolean single) {
        return entityArgument(name, enableEntities, single, null);
    }

    public ArgumentBuilder entityArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return entityArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder entityArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return entityArgument(name, true, false, suggestionAction, contextAction);
    }

    public ArgumentBuilder entityArgument(@NotNull String name, boolean enableEntities, boolean single, @Nullable SuggestionAction suggestionAction) {
        return entityArgument(name, enableEntities, single, suggestionAction, null);
    }

    public ArgumentBuilder entityArgument(@NotNull String name, boolean enableEntities, boolean single, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new EntityArgument(name, suggestionAction, contextAction, enableEntities, single));
        return this;
    }

    public <T extends Enum<T>> ArgumentBuilder enumArgument(@NotNull String name, @NotNull Class<T> clazz) {
        return enumArgument(name, clazz, null);
    }

    public <T extends Enum<T>> ArgumentBuilder enumArgument(@NotNull String name, @NotNull Class<T> clazz, @Nullable Predicate<T> filter) {
        return enumArgument(name, clazz, filter, null);
    }

    public <T extends Enum<T>> ArgumentBuilder enumArgument(@NotNull String name, @NotNull Class<T> clazz, @Nullable Predicate<T> filter, @Nullable ContextAction contextAction) {
        arguments.add(new EnumArgument<>(name, clazz, filter, contextAction));
        return this;
    }

    public ArgumentBuilder floatArgument(@NotNull String name) {
        return floatArgument(name, null);
    }

    public ArgumentBuilder floatArgument(@NotNull String name, Float min, Float max) {
        return floatArgument(name, min, max, null);
    }

    public ArgumentBuilder floatArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return floatArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder floatArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return floatArgument(name, -Float.MAX_VALUE, Float.MAX_VALUE, suggestionAction, contextAction);
    }

    public ArgumentBuilder floatArgument(@NotNull String name, Float min, Float max, @Nullable SuggestionAction suggestionAction) {
        return floatArgument(name, min, max, suggestionAction, null);
    }

    public ArgumentBuilder floatArgument(@NotNull String name, Float min, Float max, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new FloatArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    public ArgumentBuilder integerArgument(@NotNull String name) {
        return integerArgument(name, null);
    }

    public ArgumentBuilder integerArgument(@NotNull String name, Integer min, Integer max) {
        return integerArgument(name, min, max, null);
    }

    public ArgumentBuilder integerArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return integerArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder integerArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return integerArgument(name, Integer.MIN_VALUE, Integer.MAX_VALUE, suggestionAction, contextAction);
    }

    public ArgumentBuilder integerArgument(@NotNull String name, Integer min, Integer max, @Nullable SuggestionAction suggestionAction) {
        return integerArgument(name, min, max, suggestionAction, null);
    }

    public ArgumentBuilder integerArgument(@NotNull String name, Integer min, Integer max, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new IntegerArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    public ArgumentBuilder itemStackArgument(@NotNull String name) {
        return itemStackArgument(name, null);
    }

    public ArgumentBuilder itemStackArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return itemStackArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder itemStackArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new ItemStackArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder literalArgument(@NotNull String name, @NotNull List<String> literals) {
        return literalArgument(name, literals, null);
    }

    public ArgumentBuilder literalArgument(@NotNull String name, @NotNull List<String> literals, @Nullable ContextAction contextAction) {
        arguments.add(new LiteralArgument(name, literals, contextAction));
        return this;
    }

    public ArgumentBuilder locationArgument(@NotNull String name) {
        return locationArgument(name, null);
    }

    public ArgumentBuilder locationArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return locationArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder locationArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new LocationArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder particleArgument(@NotNull String name) {
        return particleArgument(name, null);
    }

    public ArgumentBuilder particleArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return particleArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder particleArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new ParticleArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder potionEffectArgument(@NotNull String name) {
        return potionEffectArgument(name, null);
    }

    public ArgumentBuilder potionEffectArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return potionEffectArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder potionEffectArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new PotionEffectArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder stringArgument(@NotNull String name) {
        return stringArgument(name, ((@Nullable SuggestionAction) null));
    }

    public ArgumentBuilder stringArgument(@NotNull String name, StringArgument.Type type) {
        return stringArgument(name, type, null);
    }

    public ArgumentBuilder stringArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return stringArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder stringArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return stringArgument(name, StringArgument.Type.PHRASE, suggestionAction, contextAction);
    }

    public ArgumentBuilder stringArgument(@NotNull String name, StringArgument.Type type, @Nullable SuggestionAction suggestionAction) {
        return stringArgument(name, type, suggestionAction, null);
    }

    public ArgumentBuilder stringArgument(@NotNull String name, StringArgument.Type type, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new StringArgument(name, suggestionAction, contextAction, type));
        return this;
    }

    public ArgumentBuilder teamArgument(@NotNull String name) {
        return teamArgument(name, null);
    }

    public ArgumentBuilder teamArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return teamArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder teamArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new TeamArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder unparsedArgument(@NotNull String name) {
        return unparsedArgument(name, null);
    }

    public ArgumentBuilder unparsedArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return unparsedArgument(name, suggestionAction, null);
    }

    public ArgumentBuilder unparsedArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new UnparsedArgument(name, suggestionAction, contextAction));
        return this;
    }

    public ArgumentBuilder execute(@NotNull ContextAction contextAction) {
        this.contextAction = contextAction;
        return this;
    }

    List<Argument<?>> build() {
        if (!arguments.isEmpty()) {
            Argument<?> last = arguments.get(arguments.size() - 1);
            if (!last.hasContextAction()) {
                last.setContextAction(contextAction);
            }
        }

        return arguments;
    }
}
