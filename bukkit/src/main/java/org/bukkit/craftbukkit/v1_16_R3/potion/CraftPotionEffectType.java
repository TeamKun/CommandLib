package org.bukkit.craftbukkit.v1_16_R3.potion;

import net.minecraft.server.v1_16_R3.MobEffectList;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CraftPotionEffectType extends PotionEffectType {

    public CraftPotionEffectType(MobEffectList handle) {
        super(MobEffectList.getId(handle));
    }

    @Override
    public double getDurationModifier() {
        return 0;
    }

    @Override
    public @NotNull String getName() {
        return null;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public @NotNull Color getColor() {
        return null;
    }
}
