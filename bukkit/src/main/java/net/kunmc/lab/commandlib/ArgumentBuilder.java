package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.argument.*;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ArgumentBuilder extends AbstractArgumentBuilder<CommandContext, Arguments, ArgumentBuilder> {
    /**
     * Add argument for {@link org.bukkit.block.data.BlockData}.
     */
    public ArgumentBuilder blockDataArgument(@NotNull String name) {
        return blockDataArgument(name, null, null);
    }

    /**
     * Add argument for {@link org.bukkit.block.data.BlockData}.
     */
    public ArgumentBuilder blockDataArgument(@NotNull String name,
                                             @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return blockDataArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.block.data.BlockData}.
     */
    public ArgumentBuilder blockDataArgument(@NotNull String name,
                                             @Nullable SuggestionAction<CommandContext> suggestionAction,
                                             @Nullable ContextAction<CommandContext> contextAction) {
        return blockDataArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.block.data.BlockData}.
     */
    public ArgumentBuilder blockDataArgumentWith(@NotNull String name,
                                                 @Nullable Consumer<Argument.Option<BlockData, CommandContext>> options) {
        return addArgument(new BlockDataArgument(name, options));
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public ArgumentBuilder boolArgument(@NotNull String name) {
        return boolArgument(name, null, null);
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public ArgumentBuilder boolArgument(@NotNull String name,
                                        @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return boolArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public ArgumentBuilder boolArgument(@NotNull String name,
                                        @Nullable SuggestionAction<CommandContext> suggestionAction,
                                        @Nullable ContextAction<CommandContext> contextAction) {
        return boolArgumentWith(name, options -> {
            options.suggestionAction(suggestionAction)
                   .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.lang.Boolean}.
     */
    public ArgumentBuilder boolArgumentWith(@NotNull String name,
                                            @Nullable Consumer<Argument.Option<Boolean, CommandContext>> options) {
        return addArgument(new BooleanArgument(name, options));
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
    public ArgumentBuilder doubleArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return doubleArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction,
                                          @Nullable ContextAction<CommandContext> contextAction) {
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
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return doubleArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgument(@NotNull String name,
                                          Double min,
                                          Double max,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction,
                                          @Nullable ContextAction<CommandContext> contextAction) {
        return doubleArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Double, CommandContext>> options) {
        return addArgument(new DoubleArgument(name, options));
    }

    /**
     * Add argument for {@link java.lang.Double}.
     */
    public ArgumentBuilder doubleArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Double, CommandContext>> options,
                                              Double min,
                                              Double max) {
        return addArgument(new DoubleArgument(name, options, min, max));
    }

    /**
     * Add argument for {@link org.bukkit.enchantments.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgument(@NotNull String name) {
        return enchantmentArgument(name, null, null);
    }

    /**
     * Add argument for {@link org.bukkit.enchantments.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgument(@NotNull String name,
                                               @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return enchantmentArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.enchantments.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgument(@NotNull String name,
                                               @Nullable SuggestionAction<CommandContext> suggestionAction,
                                               @Nullable ContextAction<CommandContext> contextAction) {
        return enchantmentArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.enchantments.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgumentWith(@NotNull String name,
                                                   @Nullable Consumer<Argument.Option<Enchantment, CommandContext>> options) {
        return addArgument(new EnchantmentArgument(name, options));
    }

    /**
     * Add argument for {@link org.bukkit.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name) {
        return entityArgument(name, null, null);
    }

    /**
     * Add argument for {@link org.bukkit.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return entityArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.entity.Entity}.
     */
    public ArgumentBuilder entityArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction,
                                          @Nullable ContextAction<CommandContext> contextAction) {
        return entityArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.entity.Entity}.
     */
    public ArgumentBuilder entityArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Entity, CommandContext>> options) {
        return addArgument(new EntityArgument(name, options));
    }

    /**
     * Add argument for {@link java.util.List} of {@link org.bukkit.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgument(@NotNull String name) {
        return entitiesArgument(name, null, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link org.bukkit.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return entitiesArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link org.bukkit.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction,
                                            @Nullable ContextAction<CommandContext> contextAction) {
        return entitiesArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.util.List} of {@link org.bukkit.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<List<Entity>, CommandContext>> options) {
        return addArgument(new EntitiesArgument(name, options));
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
    public ArgumentBuilder floatArgument(@NotNull String name,
                                         @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return floatArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name,
                                         @Nullable SuggestionAction<CommandContext> suggestionAction,
                                         @Nullable ContextAction<CommandContext> contextAction) {
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
                                         @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return floatArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgument(@NotNull String name,
                                         Float min,
                                         Float max,
                                         @Nullable SuggestionAction<CommandContext> suggestionAction,
                                         @Nullable ContextAction<CommandContext> contextAction) {
        return floatArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgumentWith(@NotNull String name,
                                             @Nullable Consumer<Argument.Option<Float, CommandContext>> options) {
        return addArgument(new FloatArgument(name, options));
    }

    /**
     * Add argument for {@link java.lang.Float}.
     */
    public ArgumentBuilder floatArgumentWith(@NotNull String name,
                                             @Nullable Consumer<Argument.Option<Float, CommandContext>> options,
                                             Float min,
                                             Float max) {
        return addArgument(new FloatArgument(name, options, min, max));
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
    public ArgumentBuilder integerArgument(@NotNull String name,
                                           @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return integerArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name,
                                           @Nullable SuggestionAction<CommandContext> suggestionAction,
                                           @Nullable ContextAction<CommandContext> contextAction) {
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
                                           @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return integerArgument(name, min, max, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgument(@NotNull String name,
                                           Integer min,
                                           Integer max,
                                           @Nullable SuggestionAction<CommandContext> suggestionAction,
                                           @Nullable ContextAction<CommandContext> contextAction) {
        return integerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, min, max);
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgumentWith(@NotNull String name,
                                               @Nullable Consumer<Argument.Option<Integer, CommandContext>> options) {
        return addArgument(new IntegerArgument(name, options));
    }

    /**
     * Add argument for {@link java.lang.Integer}.
     */
    public ArgumentBuilder integerArgumentWith(@NotNull String name,
                                               @Nullable Consumer<Argument.Option<Integer, CommandContext>> options,
                                               Integer min,
                                               Integer max) {
        return addArgument(new IntegerArgument(name, options, min, max));
    }

    /**
     * Add argument for {@link org.bukkit.inventory.ItemStack}.
     */
    public ArgumentBuilder itemStackArgument(@NotNull String name) {
        return itemStackArgument(name, null);
    }

    /**
     * Add argument for {@link org.bukkit.inventory.ItemStack}.
     */
    public ArgumentBuilder itemStackArgument(@NotNull String name,
                                             @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return itemStackArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.inventory.ItemStack}.
     */
    public ArgumentBuilder itemStackArgument(@NotNull String name,
                                             @Nullable SuggestionAction<CommandContext> suggestionAction,
                                             @Nullable ContextAction<CommandContext> contextAction) {
        return itemStackArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.inventory.ItemStack}.
     */
    public ArgumentBuilder itemStackArgumentWith(@NotNull String name,
                                                 @Nullable Consumer<Argument.Option<ItemStack, CommandContext>> options) {
        return addArgument(new ItemStackArgument(name, options));
    }

    /**
     * Add argument for {@link org.bukkit.Location}.
     */
    public ArgumentBuilder locationArgument(@NotNull String name) {
        return locationArgument(name, null);
    }

    /**
     * Add argument for {@link org.bukkit.Location}.
     */
    public ArgumentBuilder locationArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return locationArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.Location}.
     */
    public ArgumentBuilder locationArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction,
                                            @Nullable ContextAction<CommandContext> contextAction) {
        return locationArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.Location}.
     */
    public ArgumentBuilder locationArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<Location, CommandContext>> options) {
        return addArgument(new LocationArgument(name, options));
    }

    /**
     * Add argument for {@link org.bukkit.OfflinePlayer}.
     */
    public ArgumentBuilder offlinePlayerArgument(@NotNull String name) {
        return offlinePlayerArgument(name, null);
    }

    /**
     * Add argument for {@link org.bukkit.OfflinePlayer}.
     */
    public ArgumentBuilder offlinePlayerArgument(@NotNull String name,
                                                 @Nullable Predicate<? super OfflinePlayer> filter) {
        return offlinePlayerArgument(name, filter, null);
    }

    /**
     * Add argument for {@link org.bukkit.OfflinePlayer}.
     */
    public ArgumentBuilder offlinePlayerArgument(@NotNull String name,
                                                 @Nullable Predicate<? super OfflinePlayer> filter,
                                                 @Nullable ContextAction<CommandContext> contextAction) {
        return offlinePlayerArgumentWith(name, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.OfflinePlayer}.
     */
    public ArgumentBuilder offlinePlayerArgumentWith(@NotNull String name,
                                                     @Nullable Consumer<Argument.Option<OfflinePlayer, CommandContext>> options) {
        return addArgument(new OfflinePlayerArgument(name, options));
    }

    /**
     * Add argument for {@link org.bukkit.Particle}.
     */
    public ArgumentBuilder particleArgument(@NotNull String name) {
        return particleArgument(name, null);
    }

    /**
     * Add argument for {@link org.bukkit.Particle}.
     */
    public ArgumentBuilder particleArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return particleArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.Particle}.
     */
    public ArgumentBuilder particleArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction,
                                            @Nullable ContextAction<CommandContext> contextAction) {
        return particleArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.Particle}.
     */
    public ArgumentBuilder particleArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<Particle, CommandContext>> options) {
        return addArgument(new ParticleArgument(name, options));
    }

    /**
     * Add argument for {@link org.bukkit.entity.Player}.
     */
    public ArgumentBuilder playerArgument(@NotNull String name) {
        return playerArgument(name, null);
    }

    /**
     * Add argument for {@link org.bukkit.entity.Player}.
     */
    public ArgumentBuilder playerArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return playerArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.entity.Player}.
     */
    public ArgumentBuilder playerArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction,
                                          @Nullable ContextAction<CommandContext> contextAction) {
        return playerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.entity.Player}.
     */
    public ArgumentBuilder playerArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Player, CommandContext>> options) {
        return addArgument(new PlayerArgument(name, options));
    }

    /**
     * Add argument for {@link java.util.List} of {@link org.bukkit.entity.Player}.
     */
    public ArgumentBuilder playersArgument(@NotNull String name) {
        return playersArgument(name, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link org.bukkit.entity.Player}.
     */
    public ArgumentBuilder playersArgument(@NotNull String name,
                                           @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return playersArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link org.bukkit.entity.Player}.
     */
    public ArgumentBuilder playersArgument(@NotNull String name,
                                           @Nullable SuggestionAction<CommandContext> suggestionAction,
                                           @Nullable ContextAction<CommandContext> contextAction) {
        return playersArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.util.List} of {@link org.bukkit.entity.Player}.
     */
    public ArgumentBuilder playersArgumentWith(@NotNull String name,
                                               @Nullable Consumer<Argument.Option<List<Player>, CommandContext>> options) {
        return addArgument(new PlayersArgument(name, options));
    }

    /**
     * Add argument for {@link org.bukkit.potion.PotionEffect}.
     */
    public ArgumentBuilder potionEffectArgument(@NotNull String name) {
        return potionEffectArgument(name, null);
    }

    /**
     * Add argument for {@link org.bukkit.potion.PotionEffect}.
     */
    public ArgumentBuilder potionEffectArgument(@NotNull String name,
                                                @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return potionEffectArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.potion.PotionEffect}.
     */
    public ArgumentBuilder potionEffectArgument(@NotNull String name,
                                                @Nullable SuggestionAction<CommandContext> suggestionAction,
                                                @Nullable ContextAction<CommandContext> contextAction) {
        return potionEffectArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.potion.PotionEffect}.
     */
    public ArgumentBuilder potionEffectArgumentWith(@NotNull String name,
                                                    @Nullable Consumer<Argument.Option<PotionEffect, CommandContext>> options) {
        return addArgument(new PotionEffectArgument(name, options));
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name) {
        return stringArgument(name, ((@Nullable SuggestionAction<CommandContext>) null));
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
    public ArgumentBuilder stringArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return stringArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction,
                                          @Nullable ContextAction<CommandContext> contextAction) {
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
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return stringArgument(name, type, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgument(@NotNull String name,
                                          @NotNull StringArgument.Type type,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction,
                                          @Nullable ContextAction<CommandContext> contextAction) {
        return stringArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        }, type);
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<String, CommandContext>> options) {
        return addArgument(new StringArgument(name, options));
    }

    /**
     * Add argument for {@link java.lang.String}.
     */
    public ArgumentBuilder stringArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<String, CommandContext>> options,
                                              @NotNull StringArgument.Type type) {
        return addArgument(new StringArgument(name, options, type));
    }

    /**
     * Add argument for {@link org.bukkit.scoreboard.Team}.
     */
    public ArgumentBuilder teamArgument(@NotNull String name) {
        return teamArgument(name, null);
    }

    /**
     * Add argument for {@link org.bukkit.scoreboard.Team}.
     */
    public ArgumentBuilder teamArgument(@NotNull String name,
                                        @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return teamArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link org.bukkit.scoreboard.Team}.
     */
    public ArgumentBuilder teamArgument(@NotNull String name,
                                        @Nullable SuggestionAction<CommandContext> suggestionAction,
                                        @Nullable ContextAction<CommandContext> contextAction) {
        return teamArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link org.bukkit.scoreboard.Team}.
     */
    public ArgumentBuilder teamArgumentWith(@NotNull String name,
                                            @Nullable Consumer<Argument.Option<Team, CommandContext>> options) {
        return addArgument(new TeamArgument(name, options));
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
    public ArgumentBuilder unparsedArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return unparsedArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.lang.String}.<br>
     * By using this, you can get raw string like @r.
     */
    public ArgumentBuilder unparsedArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction,
                                            @Nullable ContextAction<CommandContext> contextAction) {
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
                                                @Nullable Consumer<Argument.Option<String, CommandContext>> options) {
        return addArgument(new UnparsedArgument(name, options));
    }

    public ArgumentBuilder uuidArgument(@NotNull String name) {
        return uuidArgumentWith(name, option -> {
        });
    }

    public ArgumentBuilder uuidArgumentWith(@NotNull String name,
                                            @Nullable Consumer<Argument.Option<UUID, CommandContext>> options) {
        return addArgument(new UUIDArgument(name, options));
    }

    public ArgumentBuilder uuidsArgument(@NotNull String name) {
        return uuidsArgumentWith(name, option -> {
        });
    }

    public ArgumentBuilder uuidsArgumentWith(@NotNull String name,
                                             @Nullable Consumer<Argument.Option<List<UUID>, CommandContext>> options) {
        return addArgument(new UUIDsArgument(name, options));
    }

    @Override
    Arguments createArguments(List<CommonArgument<?, CommandContext>> commonArguments) {
        return new Arguments(commonArguments);
    }
}
