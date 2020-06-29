package dev.revivalmodding.metaverse.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerData extends INBTSerializable<CompoundNBT> {

    void sync();
}
