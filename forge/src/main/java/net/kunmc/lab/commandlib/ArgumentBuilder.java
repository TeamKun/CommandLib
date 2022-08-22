package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class ArgumentBuilder {
    private final List<Argument<?>> arguments = new ArrayList<>();
    private ContextAction contextAction = null;

    /**
     * Add argument for {@link net.minecraft.util.math.BlockPos}.
     */
    public ArgumentBuilder blockPosArgument(@NotNull String name) {
        return blockPosArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.util.math.BlockPos}.
     */
    public ArgumentBuilder blockPosArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return blockPosArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.util.math.BlockPos}.
     */
    public ArgumentBuilder blockPosArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new BlockPosArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public ArgumentBuilder boolArgument(@NotNull String name) {
        return boolArgument(name, null);
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public ArgumentBuilder boolArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return boolArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public ArgumentBuilder boolArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new BooleanArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for {@link net.minecraft.command.arguments.BlockStateInput}.
     */
    public ArgumentBuilder blockStateArgument(@NotNull String name) {
        return blockStateArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.command.arguments.BlockStateInput}.
     */
    public ArgumentBuilder blockStateArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return blockStateArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.command.arguments.BlockStateInput}.
     */
    public ArgumentBuilder blockStateArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new BlockStateArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name) {
        return doubleArgument(name, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name, Double min, Double max) {
        return doubleArgument(name, min, max, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return doubleArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return doubleArgument(name, -Double.MAX_VALUE, Double.MAX_VALUE, suggestionAction, contextAction);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name, Double min, Double max, @Nullable SuggestionAction suggestionAction) {
        return doubleArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name, Double min, Double max, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new DoubleArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    /**
     * Add argument for {@link net.minecraft.potion.Effect}.
     */
    public ArgumentBuilder effectArgument(@NotNull String name) {
        return effectArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.potion.Effect}.
     */
    public ArgumentBuilder effectArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return effectArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.potion.Effect}.
     */
    public ArgumentBuilder effectArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new EffectArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for {@link net.minecraft.enchantment.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgument(@NotNull String name) {
        return enchantmentArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.enchantment.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return enchantmentArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.enchantment.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, ContextAction contextAction) {
        arguments.add(new EnchantmentArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name) {
        return entityArgument(name, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.<br>
     * If {@code enableEntities} is true, then includes Entity({@link net.minecraft.entity.player.PlayerEntity}, {@link net.minecraft.entity.MobEntity}...), else only includes {@link net.minecraft.entity.player.PlayerEntity}.<br>
     * If {@code single} is true, then includes only one Entity, else there is possibility to include two or more Entity.
     */
    public ArgumentBuilder entityArgument(@NotNull String name, boolean enableEntities, boolean single) {
        return entityArgument(name, enableEntities, single, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return entityArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return entityArgument(name, true, false, suggestionAction, contextAction);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.<br>
     * If {@code enableEntities} is true, then includes Entity({@link net.minecraft.entity.player.PlayerEntity}, {@link net.minecraft.entity.MobEntity}...), else only includes {@link net.minecraft.entity.player.PlayerEntity}.<br>
     * If {@code single} is true, then includes only one Entity, else there is possibility to include two or more Entity.
     */
    public ArgumentBuilder entityArgument(@NotNull String name, boolean enableEntities, boolean single, @Nullable SuggestionAction suggestionAction) {
        return entityArgument(name, enableEntities, single, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.<br>
     * If {@code enableEntities} is true, then includes Entity({@link net.minecraft.entity.player.PlayerEntity}, {@link net.minecraft.entity.MobEntity}...), else only includes {@link net.minecraft.entity.player.PlayerEntity}.<br>
     * If {@code single} is true, then includes only one Entity, else there is possibility to include two or more Entity.
     */
    public ArgumentBuilder entityArgument(@NotNull String name, boolean enableEntities, boolean single, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new EntityArgument(name, suggestionAction, contextAction, enableEntities, single));
        return this;
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public <T extends Enum<T>> ArgumentBuilder enumArgument(@NotNull String name, @NotNull Class<T> clazz) {
        return enumArgument(name, clazz, null);
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public <T extends Enum<T>> ArgumentBuilder enumArgument(@NotNull String name, @NotNull Class<T> clazz, @Nullable Predicate<? super T> filter) {
        return enumArgument(name, clazz, filter, null);
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public <T extends Enum<T>> ArgumentBuilder enumArgument(@NotNull String name, @NotNull Class<T> clazz, @Nullable Predicate<? super T> filter, @Nullable ContextAction contextAction) {
        arguments.add(new EnumArgument<>(name, clazz, filter, contextAction));
        return this;
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name) {
        return floatArgument(name, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name, Float min, Float max) {
        return floatArgument(name, min, max, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return floatArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return floatArgument(name, -Float.MAX_VALUE, Float.MAX_VALUE, suggestionAction, contextAction);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name, Float min, Float max, @Nullable SuggestionAction suggestionAction) {
        return floatArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name, Float min, Float max, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new FloatArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name) {
        return integerArgument(name, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name, Integer min, Integer max) {
        return integerArgument(name, min, max, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return integerArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return integerArgument(name, Integer.MIN_VALUE, Integer.MAX_VALUE, suggestionAction, contextAction);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name, Integer min, Integer max, @Nullable SuggestionAction suggestionAction) {
        return integerArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name, Integer min, Integer max, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new IntegerArgument(name, suggestionAction, contextAction, min, max));
        return this;
    }

    /**
     * Add argument for {@link net.minecraft.item.ItemStack}.
     */
    public ArgumentBuilder itemStackArgument(@NotNull String name) {
        return itemStackArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.item.ItemStack}.
     */
    public ArgumentBuilder itemStackArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return itemStackArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.item.ItemStack}.
     */
    public ArgumentBuilder itemStackArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new ItemStackArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * It is only possible to include a string specified by {@code literals}
     */
    public ArgumentBuilder literalArgument(@NotNull String name, @NotNull Collection<String> literals) {
        return literalArgument(name, literals, null);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * It is only possible to include a string specified by {@code literals}
     */
    public ArgumentBuilder literalArgument(@NotNull String name, @NotNull Collection<String> literals, @Nullable ContextAction contextAction) {
        arguments.add(new LiteralArgument(name, literals, contextAction));
        return this;
    }

    /**
     * Add argument for {@link net.kunmc.lab.commandlib.util.Location}.
     */
    public ArgumentBuilder locationArgument(@NotNull String name) {
        return locationArgument(name, null);
    }

    /**
     * Add argument for {@link net.kunmc.lab.commandlib.util.Location}.
     */
    public ArgumentBuilder locationArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return locationArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.kunmc.lab.commandlib.util.Location}.
     */
    public ArgumentBuilder locationArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new LocationArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public <T extends Nameable> ArgumentBuilder objectArgument(@NotNull String name, @NotNull Collection<? extends T> candidates) {
        return objectArgument(name, candidates, null);
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public <T extends Nameable> ArgumentBuilder objectArgument(@NotNull String name, @NotNull Collection<? extends T> candidates, @Nullable Predicate<? super T> filter) {
        return objectArgument(name, candidates, null, null);
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public <T extends Nameable> ArgumentBuilder objectArgument(@NotNull String name, @NotNull Collection<? extends T> candidates, @Nullable Predicate<? super T> filter, @Nullable ContextAction contextAction) {
        arguments.add(new ObjectArgument<>(name, candidates, filter, contextAction));
        return this;
    }

    /**
     * Add argument for {@link net.minecraft.particles.IParticleData}.
     */
    public ArgumentBuilder particleArgument(@NotNull String name) {
        return particleArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.particles.IParticleData}.
     */
    public ArgumentBuilder particleArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return particleArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.particles.IParticleData}.
     */
    public ArgumentBuilder particleArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new ParticleArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name) {
        return stringArgument(name, ((@Nullable SuggestionAction) null));
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name, StringArgument.Type type) {
        return stringArgument(name, type, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return stringArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        return stringArgument(name, StringArgument.Type.PHRASE, suggestionAction, contextAction);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name, StringArgument.Type type, @Nullable SuggestionAction suggestionAction) {
        return stringArgument(name, type, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name, StringArgument.Type type, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new StringArgument(name, suggestionAction, contextAction, type));
        return this;
    }

    /**
     * Add argument for {@link net.minecraft.scoreboard.ScorePlayerTeam}.
     */
    public ArgumentBuilder teamArgument(@NotNull String name) {
        return teamArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.scoreboard.ScorePlayerTeam}.
     */
    public ArgumentBuilder teamArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return teamArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.scoreboard.ScorePlayerTeam}.
     */
    public ArgumentBuilder teamArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new TeamArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * By using this, you can get raw string like @r.
     */
    public ArgumentBuilder unparsedArgument(@NotNull String name) {
        return unparsedArgument(name, null);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * By using this, you can get raw string like @r.
     */
    public ArgumentBuilder unparsedArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return unparsedArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * By using this, you can get raw string like @r.
     */
    public ArgumentBuilder unparsedArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction, @Nullable ContextAction contextAction) {
        arguments.add(new UnparsedArgument(name, suggestionAction, contextAction));
        return this;
    }

    /**
     * Set command's process.<br>
     * If arguments are not added, process set by this wouldn't work. Then you should override {@link net.kunmc.lab.commandlib.Command#execute(CommandContext)}
     */
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
