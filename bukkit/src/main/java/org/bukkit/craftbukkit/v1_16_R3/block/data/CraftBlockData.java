package org.bukkit.craftbukkit.v1_16_R3.block.data;

import net.minecraft.server.v1_16_R3.IBlockData;
import org.bukkit.Material;
import org.bukkit.SoundGroup;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftBlockData implements BlockData {
    public static CraftBlockData createData(IBlockData iBlockData) {
        return null;
    }

    @Override
    public @NotNull Material getMaterial() {
        return null;
    }

    @Override
    public @NotNull String getAsString() {
        return null;
    }

    @Override
    public @NotNull String getAsString(boolean hideUnspecified) {
        return null;
    }

    @Override
    public @NotNull BlockData merge(@NotNull BlockData data) {
        return null;
    }

    @Override
    public boolean matches(@Nullable BlockData data) {
        return false;
    }

    @Override
    public @NotNull BlockData clone() {
        return null;
    }

    @Override
    public @NotNull SoundGroup getSoundGroup() {
        return null;
    }
}
