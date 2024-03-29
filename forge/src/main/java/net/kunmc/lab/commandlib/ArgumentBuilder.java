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

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ArgumentBuilder extends AbstractArgumentBuilder<CommandContext, ArgumentBuilder> {
    /**
     * Add argument for {@link net.minecraft.util.math.BlockPos}.
     */
    public ArgumentBuilder blockPosArgument(@NotNull String name) {
        return blockPosArgument(name, null);
    }

    /**
     * Add argument for {@link net.minecraft.util.math.BlockPos}.
     */
    public ArgumentBuilder blockPosArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return blockPosArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.util.math.BlockPos}.
     */
    public ArgumentBuilder blockPosArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction,
                                            @Nullable ContextAction<CommandContext> contextAction) {
        return blockPosArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.util.math.BlockPos}.
     */
    public ArgumentBuilder blockPosArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<BlockPos, CommandContext>> options) {
        return addArgument(new BlockPosArgument(name, options));
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
    public ArgumentBuilder blockStateArgument(@NotNull String name,
                                              @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return blockStateArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.command.arguments.BlockStateInput}.
     */
    public ArgumentBuilder blockStateArgument(@NotNull String name,
                                              @Nullable SuggestionAction<CommandContext> suggestionAction,
                                              @Nullable ContextAction<CommandContext> contextAction) {
        return blockStateArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.command.arguments.BlockStateInput}.
     */
    public ArgumentBuilder blockStateArgumentWith(@NotNull String name,
                                                  @Nullable Consumer<Argument.Option<BlockStateInput, CommandContext>> options) {
        return addArgument(new BlockStateArgument(name, options));
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
    public ArgumentBuilder effectArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return effectArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.potion.Effect}.
     */
    public ArgumentBuilder effectArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction,
                                          ContextAction<CommandContext> contextAction) {
        return effectArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.potion.Effect}.
     */
    public ArgumentBuilder effectArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Effect, CommandContext>> options) {
        return addArgument(new EffectArgument(name, options));
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
    public ArgumentBuilder enchantmentArgument(@NotNull String name,
                                               @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return enchantmentArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.enchantment.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgument(@NotNull String name,
                                               @Nullable SuggestionAction<CommandContext> suggestionAction,
                                               ContextAction<CommandContext> contextAction) {
        return enchantmentArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link net.minecraft.enchantment.Enchantment}.
     */
    public ArgumentBuilder enchantmentArgumentWith(@NotNull String name,
                                                   @Nullable Consumer<Argument.Option<Enchantment, CommandContext>> options) {
        return addArgument(new EnchantmentArgument(name, options));
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
    public ArgumentBuilder entityArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return entityArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.entity.Entity}.
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
     * Add argument for {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entityArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<Entity, CommandContext>> options) {
        return addArgument(new EntityArgument(name, options));
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
    public ArgumentBuilder entitiesArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return entitiesArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
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
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.Entity}.
     */
    public ArgumentBuilder entitiesArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<List<Entity>, CommandContext>> options) {
        return addArgument(new EntitiesArgument(name, options));
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
                                               @Nullable ContextAction<CommandContext> contextAction) {
        return gameProfileArgumentWith(name, option -> {
            option.filter(filter)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link com.mojang.authlib.GameProfile}.
     */
    public ArgumentBuilder gameProfileArgumentWith(@NotNull String name,
                                                   @Nullable Consumer<Argument.Option<GameProfile, CommandContext>> options) {
        return addArgument(new GameProfileArgument(name, options));
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
    public ArgumentBuilder itemStackArgument(@NotNull String name,
                                             @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return itemStackArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.item.ItemStack}.
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
     * Add argument for {@link net.minecraft.item.ItemStack}.
     */
    public ArgumentBuilder itemStackArgumentWith(@NotNull String name,
                                                 @Nullable Consumer<Argument.Option<ItemStack, CommandContext>> options) {
        return addArgument(new ItemStackArgument(name, options));
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
    public ArgumentBuilder locationArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return locationArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.kunmc.lab.commandlib.util.Location}.
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
     * Add argument for {@link net.kunmc.lab.commandlib.util.Location}.
     */
    public ArgumentBuilder locationArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<Location, CommandContext>> options) {
        return addArgument(new LocationArgument(name, options));
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
    public ArgumentBuilder particleArgument(@NotNull String name,
                                            @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return particleArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.particles.IParticleData}.
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
     * Add argument for {@link net.minecraft.particles.IParticleData}.
     */
    public ArgumentBuilder particleArgumentWith(@NotNull String name,
                                                @Nullable Consumer<Argument.Option<IParticleData, CommandContext>> options) {
        return addArgument(new ParticleArgument(name, options));
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
    public ArgumentBuilder playerArgument(@NotNull String name,
                                          @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return playerArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.entity.player.ServerPlayerEntity}.
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
     * Add argument for {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playerArgumentWith(@NotNull String name,
                                              @Nullable Consumer<Argument.Option<ServerPlayerEntity, CommandContext>> options) {
        return addArgument(new PlayerArgument(name, options));
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
    public ArgumentBuilder playersArgument(@NotNull String name,
                                           @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return playersArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playersArgument(@NotNull String name,
                                           @Nullable SuggestionAction<CommandContext> suggestionAction,
                                           @Nullable ContextAction<CommandContext> contextAction) {
        return playerArgumentWith(name, option -> {
            option.suggestionAction(suggestionAction)
                  .contextAction(contextAction);
        });
    }

    /**
     * Add argument for {@link java.util.List} of {@link net.minecraft.entity.player.ServerPlayerEntity}.
     */
    public ArgumentBuilder playersArgumentWith(@NotNull String name,
                                               @Nullable Consumer<Argument.Option<List<ServerPlayerEntity>, CommandContext>> options) {
        return addArgument(new PlayersArgument(name, options));
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
    public ArgumentBuilder teamArgument(@NotNull String name,
                                        @Nullable SuggestionAction<CommandContext> suggestionAction) {
        return teamArgument(name, suggestionAction, null);
    }

    /**
     * Add argument for {@link net.minecraft.scoreboard.ScorePlayerTeam}.
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
     * Add argument for {@link net.minecraft.scoreboard.ScorePlayerTeam}.
     */
    public ArgumentBuilder teamArgumentWith(@NotNull String name,
                                            @Nullable Consumer<Argument.Option<ScorePlayerTeam, CommandContext>> options) {
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
}
