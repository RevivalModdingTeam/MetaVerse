package dev.revivalmodding.metaverse.common.capability;

import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import dev.revivalmodding.metaverse.metapower.Metapower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerData extends INBTSerializable<CompoundNBT> {

    Abilities getPlayerAbilities();

    void setMetapower(Metapower metapower);

    Metapower getMetapower();

    void sync();

    void playerTick();

    void livingTick();

    PlayerEntity getOwner();
}
