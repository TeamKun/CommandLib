package net.kunmc.lab.commandlib;

import com.mojang.authlib.GameProfile;
import net.kunmc.lab.commandlib.argument.*;
import net.kunmc.lab.commandlib.util.Location;
import net.minecraft.command.arguments.BlockStateInput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.Effect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ArgumentBuilder {
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
    public ArgumentBuilder blockPosArgument(@NotNull String name,
                                            @Nullable SuggestionAction suggestionAction,
                                            @Nullable ContextAction contextAction) {
        return blockPosArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.util.math.BlockPos}.
     */
    public ArgumentBuilder blockPosArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<BlockPos>> options) {
        arguments.add(new BlockPosArgument(name, options));
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
    public ArgumentBuilder boolArgument(@NotNull String name,
                                        @Nullable SuggestionAction suggestionAction,
                                        @Nullable ContextAction contextAction) {
        return boolArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public ArgumentBuilder boolArgumentWith(@NotNull String name,
                                            @Nullable Consumer<Argument.Option<Boolean>> options) {
        arguments.add(new BooleanArgument(name, options));
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
    public ArgumentBuilder blockStateArgument(@NotNull String name,
                                              @Nullable SuggestionAction suggestionAction,
                                              @Nullable ContextAction contextAction) {
        return blockStateArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.command.arguments.BlockStateInput}.
     */
    public ArgumentBuilder blockStateArgumentWith(@NotNull String name,
                                                  @Nullable Consumer<Argument.Option<BlockStateInput>> options) {
        arguments.add(new BlockStateArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link net.kunmc.lab.commandlib.Argument} Object implemented by you.
     */
    public ArgumentBuilder customArgument(@NotNull Argument<?> argument) {
        arguments.add(argument);
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
    public ArgumentBuilder doubleArgument(@NotNull String name,
                                          @Nullable SuggestionAction suggestionAction,
                                          @Nullable ContextAction contextAction) {
        return doubleArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name,
                                          Double min,
                                          Double max,
                                          @Nullable SuggestionAction suggestionAction) {
        return doubleArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name,
                                          Double min,
                                          Double max,
                                          @Nullable SuggestionAction suggestionAction,
                                          @Nullable ContextAction contextAction) {
        return doubleArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Double>> options) {
        arguments.add(new DoubleArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Double>> options,
                                              Double min,
                                              Double max) {
        arguments.add(new DoubleArgument(name, options, min, max));
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
    public ArgumentBuilder effectArgument(@NotNull String name,
                                          @Nullable SuggestionAction suggestionAction,
                                          ContextAction contextAction) {
        return effectArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.potion.Effect}.
     */
    public ArgumentBuilder effectArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Effect>> options) {
        arguments.add(new EffectArgument(name, options));
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
    public ArgumentBuilder enchantmentArgument(@NotNull String name,
                                               @Nullable SuggestionAction suggestionAction,
                                               ContextAction contextAction) {
        return enchantmentArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.enchantment.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgumentWith(@NotNull String name,
                                                   @Nullable Consumer<Argument.Option<Enchantment>> options) {
        arguments.add(new EnchantmentArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name) {
        return entityArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return entityArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name,
                                          @Nullable SuggestionAction suggestionAction,
                                          @Nullable ContextAction contextAction) {
        return entityArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entityArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Entity>> options) {
        arguments.add(new EntityArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgument(@NotNull String name) {
        return entitiesArgument(name, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return entitiesArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgument(@NotNull String name,
                                            @Nullable SuggestionAction suggestionAction,
                                            @Nullable ContextAction contextAction) {
        return entitiesArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<List<Entity>>> options) {
        arguments.add(new EntitiesArgument(name, options));
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
    public <T extends Enum<T>> ArgumentBuilder enumArgument(@NotNull String name,
                                                            @NotNull Class<T> clazz,
                                                            @Nullable Predicate<? super T> filter) {
        return enumArgument(name, clazz, filter, null);
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public <T extends Enum<T>> ArgumentBuilder enumArgument(@NotNull String name,
                                                            @NotNull Class<T> clazz,
                                                            @Nullable Predicate<? super T> filter,
                                                            @Nullable ContextAction contextAction) {
        return enumArgumentWith(name, clazz, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for T extends {@link java.lang.Enum}.
     */
    public <T extends Enum<T>> ArgumentBuilder enumArgumentWith(@NotNull String name,
                                                                @NotNull Class<T> clazz,
                                                                @Nullable Consumer<Argument.Option<T>> options) {
        arguments.add(new EnumArgument<>(name, clazz, options));
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
    public ArgumentBuilder floatArgument(@NotNull String name,
                                         @Nullable SuggestionAction suggestionAction,
                                         @Nullable ContextAction contextAction) {
        return floatArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name,
                                         Float min,
                                         Float max,
                                         @Nullable SuggestionAction suggestionAction) {
        return floatArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name,
                                         Float min,
                                         Float max,
                                         @Nullable SuggestionAction suggestionAction,
                                         @Nullable ContextAction contextAction) {
        return floatArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgumentWith(@NotNull String name, @Nullable Consumer<Argument.Option<Float>> options) {
        arguments.add(new FloatArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgumentWith(@NotNull String name,
                                             @Nullable Consumer<Argument.Option<Float>> options,
                                             Float min,
                                             Float max) {
        arguments.add(new FloatArgument(name, options, min, max));
        return this;
    }

    /**
     * Add argument for {@link com.mojang.authlib.GameProfile}.
     */
    public ArgumentBuilder gameProfileArgument(@NotNull String name) {
        return gameProfileArgument(name, null);
    }

    /**
     * Add argument for {@link com.mojang.authlib.GameProfile}.
     */
    public ArgumentBuilder gameProfileArgument(@NotNull String name, @Nullable Predicate<? super GameProfile> filter) {
        return gameProfileArgument(name, filter, null);
    }

    /**
     * Add argument for {@link com.mojang.authlib.GameProfile}.
     */
    public ArgumentBuilder gameProfileArgument(@NotNull String name,
                                               @Nullable Predicate<? super GameProfile> filter,
                                               @Nullable ContextAction contextAction) {
        return gameProfileArgumentWith(name, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link com.mojang.authlib.GameProfile}.
     */
    public ArgumentBuilder gameProfileArgumentWith(@NotNull String name,
                                                   @Nullable Consumer<Argument.Option<GameProfile>> options) {
        arguments.add(new GameProfileArgument(name, options));
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
    public ArgumentBuilder integerArgument(@NotNull String name,
                                           @Nullable SuggestionAction suggestionAction,
                                           @Nullable ContextAction contextAction) {
        return integerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name,
                                           Integer min,
                                           Integer max,
                                           @Nullable SuggestionAction suggestionAction) {
        return integerArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name,
                                           Integer min,
                                           Integer max,
                                           @Nullable SuggestionAction suggestionAction,
                                           @Nullable ContextAction contextAction) {
        return integerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgumentWith(@NotNull String name,
                                               @Nullable Consumer<Argument.Option<Integer>> options) {
        arguments.add(new IntegerArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgumentWith(@NotNull String name,
                                               @Nullable Consumer<Argument.Option<Integer>> options,
                                               Integer min,
                                               Integer max) {
        arguments.add(new IntegerArgument(name, options, min, max));
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
    public ArgumentBuilder itemStackArgument(@NotNull String name,
                                             @Nullable SuggestionAction suggestionAction,
                                             @Nullable ContextAction contextAction) {
        return itemStackArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.item.ItemStack}.
     */
    public ArgumentBuilder itemStackArgumentWith(@NotNull String name,
                                                 @Nullable Consumer<Argument.Option<ItemStack>> options) {
        arguments.add(new ItemStackArgument(name, options));
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
    public ArgumentBuilder literalArgument(@NotNull String name,
                                           @NotNull Collection<String> literals,
                                           @Nullable ContextAction contextAction) {
        return literalArgument(name, () -> literals, contextAction);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * It is only possible to include a string specified by {@code literals}
     */
    public ArgumentBuilder literalArgument(@NotNull String name,
                                           @NotNull Supplier<Collection<String>> literalsSupplier) {
        return literalArgument(name, literalsSupplier, null);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * It is only possible to include a string specified by {@code literals}
     */
    public ArgumentBuilder literalArgument(@NotNull String name,
                                           @NotNull Supplier<Collection<String>> literalsSupplier,
                                           @Nullable ContextAction contextAction) {
        arguments.add(new LiteralArgument(name, literalsSupplier, contextAction));
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
    public ArgumentBuilder locationArgument(@NotNull String name,
                                            @Nullable SuggestionAction suggestionAction,
                                            @Nullable ContextAction contextAction) {
        return locationArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.kunmc.lab.commandlib.util.Location}.
     */
    public ArgumentBuilder locationArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<Location>> options) {
        arguments.add(new LocationArgument(name, options));
        return this;
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public <T extends Nameable> ArgumentBuilder nameableObjectArgument(@NotNull String name,
                                                                       @NotNull Collection<? extends T> candidates) {
        return nameableObjectArgument(name, candidates, null);
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public <T extends Nameable> ArgumentBuilder nameableObjectArgument(@NotNull String name,
                                                                       @NotNull Collection<? extends T> candidates,
                                                                       @Nullable Predicate<? super T> filter) {
        return nameableObjectArgument(name, candidates, filter, null);
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public <T extends Nameable> ArgumentBuilder nameableObjectArgument(@NotNull String name,
                                                                       @NotNull Collection<? extends T> candidates,
                                                                       @Nullable Predicate<? super T> filter,
                                                                       @Nullable ContextAction contextAction) {
        return nameableObjectArgumentWith(name, candidates, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for object that implements {@link net.kunmc.lab.commandlib.Nameable}.<br>
     * It is only possible to include an object specified by {@code candidates}
     */
    public <T extends Nameable> ArgumentBuilder nameableObjectArgumentWith(@NotNull String name,
                                                                           @NotNull Collection<? extends T> candidates,
                                                                           @Nullable Consumer<Argument.Option<T>> options) {
        arguments.add(new NameableObjectArgument<>(name, candidates, options));
        return this;
    }

    public <T> ArgumentBuilder objectArgument(@NotNull String name, @NotNull Map<String, ? extends T> nameToObjectMap) {
        return objectArgumentWith(name, nameToObjectMap, options -> {
        });
    }

    public <T> ArgumentBuilder objectArgument(@NotNull String name,
                                              @NotNull Map<String, ? extends T> nameToObjectMap,
                                              @Nullable Predicate<? super T> filter) {
        return objectArgument(name, nameToObjectMap, filter, null);
    }

    public <T> ArgumentBuilder objectArgument(@NotNull String name,
                                              @NotNull Map<String, ? extends T> nameToObjectMap,
                                              @Nullable Predicate<? super T> filter,
                                              @Nullable ContextAction contextAction) {
        return objectArgumentWith(name, nameToObjectMap, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    public <T> ArgumentBuilder objectArgumentWith(@NotNull String name,
                                                  @NotNull Map<String, ? extends T> nameToObjectMap,
                                                  @Nullable Consumer<Argument.Option<T>> options) {
        arguments.add(new ObjectArgument<>(name, nameToObjectMap, options));
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
    public ArgumentBuilder particleArgument(@NotNull String name,
                                            @Nullable SuggestionAction suggestionAction,
                                            @Nullable ContextAction contextAction) {
        return particleArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.particles.IParticleData}.
     */
    public ArgumentBuilder particleArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<IParticleData>> options) {
        arguments.add(new ParticleArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playerArgument(@NotNull String name) {
        return playerArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playerArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return playerArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playerArgument(@NotNull String name,
                                          @Nullable SuggestionAction suggestionAction,
                                          @Nullable ContextAction contextAction) {
        return playerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playerArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<ServerPlayerEntity>> options) {
        arguments.add(new PlayerArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playersArgument(@NotNull String name) {
        return playersArgument(name, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playersArgument(@NotNull String name, @Nullable SuggestionAction suggestionAction) {
        return playersArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playersArgument(@NotNull String name,
                                           @Nullable SuggestionAction suggestionAction,
                                           @Nullable ContextAction contextAction) {
        return playerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playersArgumentWith(@NotNull String name,
                                               @Nullable Consumer<Argument.Option<List<ServerPlayerEntity>>> options) {
        arguments.add(new PlayersArgument(name, options));
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
    public ArgumentBuilder stringArgument(@NotNull String name, @NotNull StringArgument.Type type) {
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
    public ArgumentBuilder stringArgument(@NotNull String name,
                                          @Nullable SuggestionAction suggestionAction,
                                          @Nullable ContextAction contextAction) {
        return stringArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name,
                                          @NotNull StringArgument.Type type,
                                          @Nullable SuggestionAction suggestionAction) {
        return stringArgument(name, type, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name,
                                          @NotNull StringArgument.Type type,
                                          @Nullable SuggestionAction suggestionAction,
                                          @Nullable ContextAction contextAction) {
        return stringArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, type);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<String>> options) {
        arguments.add(new StringArgument(name, options));
        return this;
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<String>> options,
                                              StringArgument.Type type) {
        arguments.add(new StringArgument(name, options, type));
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
    public ArgumentBuilder teamArgument(@NotNull String name,
                                        @Nullable SuggestionAction suggestionAction,
                                        @Nullable ContextAction contextAction) {
        return teamArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.scoreboard.ScorePlayerTeam}.
     */
    public ArgumentBuilder teamArgumentWith(@NotNull String name,
                                            @Nullable Consumer<Argument.Option<ScorePlayerTeam>> options) {
        arguments.add(new TeamArgument(name, options));
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
    public ArgumentBuilder unparsedArgument(@NotNull String name,
                                            @Nullable SuggestionAction suggestionAction,
                                            @Nullable ContextAction contextAction) {
        return unparsedArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * By using this, you can get raw string like @r.
     */
    public ArgumentBuilder unparsedArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<String>> options) {
        arguments.add(new UnparsedArgument(name, options));
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
